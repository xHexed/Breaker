package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.PacketPlayInBlockDig;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static com.github.xhexed.breaker.Breaker.getPlugin;
import static org.bukkit.Bukkit.getPluginManager;

public class BreakingCore {
    static final Map<Integer, HashMap<String, BreakingBlock>> cachedBlocks = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static void handlePacket(final Object object, final Player player) {
        if (object instanceof PacketPlayInBlockDig && !player.getGameMode().equals(GameMode.CREATIVE)) {
            final PacketPlayInBlockDig packet = (PacketPlayInBlockDig) object;
            final BlockPosition blockPosition = packet.a();
            final Block block = player.getWorld().getBlockAt(
                    blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
            if (block.getType() == Material.AIR ||
                    !getPlugin().database.has(block.getType(), block.getData())
            ) return;
            final BreakingBlock breakingBlock;
            final int id = getBlockEntityId(block);
            if (packet.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                final PreBlockDamageEvent event;
                final int breakTime = getPlugin().database.get(block.getType(), block.getData());
                if (cachedBlocks.containsKey(id)) {
                    final HashMap<String, BreakingBlock> list = cachedBlocks.get(id);
                    final String name = player.getName();
                    if (list.containsKey(name)) {
                        breakingBlock = list.get(name);
                        event             = new PreBlockDamageEvent(block, player, breakTime, breakingBlock.getStage(), breakingBlock.getTimeBroken(), breakingBlock.getLastItem());
                        getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return;
                        }
                        breakingBlock.update(event);
                    }
                    else {
                        event = new PreBlockDamageEvent(block, player, breakTime, 0, 0, player.getInventory().getItemInMainHand());
                        getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return;
                        }
                        breakingBlock = new BreakingBlock(event);
                        list.put(player.getName(), breakingBlock);
                    }
                }
                else {
                    event = new PreBlockDamageEvent(block, player, breakTime, 0, 0, player.getInventory().getItemInMainHand());
                    getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    final HashMap<String, BreakingBlock> list = new HashMap<>();
                    breakingBlock = new BreakingBlock(event);
                    list.put(player.getName(), breakingBlock);
                    cachedBlocks.put(id, list);
                }
                breakingBlock.start();
            }
            else {
                if (cachedBlocks.containsKey(id)) {
                    final HashMap<String, BreakingBlock> list = cachedBlocks.get(id);
                    final String name = player.getName();
                    if (list.containsKey(name)) {
                        breakingBlock = list.get(name);
                        breakingBlock.cancel();
                    }
                }
            }
        }
    }

    public static int getBlockEntityId(final Block block) {
        return (block.getX() & 0xFFF) << 20 | (block.getZ() & 0xFFF) << 8 | block.getY() & 0xFF;
    }

}

