package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import com.github.xhexed.breaker.manager.Database;
import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.PacketPlayInBlockDig;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getPluginManager;

public class BreakingCore {
    static final Map<Long, HashMap<String, BreakingBlock>> cachedBlocks = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static void handlePacket(final Object object, final Player player) {
        if (object instanceof PacketPlayInBlockDig && player.getGameMode() != GameMode.CREATIVE) {
            final PacketPlayInBlockDig packet = (PacketPlayInBlockDig) object;
            final BlockPosition blockPosition = packet.a();
            final Block block = player.getWorld().getBlockAt(
                    blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());

            if (block.getType() == Material.AIR ||
                    !Database.has(block.getType(), block.getData())
            ) return;

            final BreakingBlock breakingBlock;
            final long id = blockPosition.asLong();

            if (packet.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                final PreBlockDamageEvent event;
                final int breakTime = Database.get(block.getType(), block.getData());

                if (cachedBlocks.containsKey(id)) {
                    final HashMap<String, BreakingBlock> list = cachedBlocks.get(id);
                    final String name = player.getName();

                    if (list.containsKey(name)) {
                        breakingBlock = list.get(name);
                        event = new PreBlockDamageEvent(block, player, breakTime, breakingBlock.getStage(), breakingBlock.getTimeBroken(), breakingBlock.getLastItem());
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
            else if (cachedBlocks.containsKey(id)) {
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

