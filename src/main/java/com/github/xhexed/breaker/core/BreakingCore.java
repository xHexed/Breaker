package com.github.xhexed.breaker.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static com.github.xhexed.breaker.Breaker.getPlugin;

public class BreakingCore {
    static final Map<Integer, HashMap<String, BreakingBlock>> cachedBlocks = new HashMap<>();

    public static void init() {
        ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(new PacketAdapter(getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {
            @SuppressWarnings("deprecation")
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                final BlockPosition bp = packet.getBlockPositionModifier().read(0);
                final Block block = player.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
                if (block == null ||
                        block.getType() == Material.AIR ||
                        !Breaker.getPlugin().database.has(block.getType(), block.getData())
                ) return;
                final BreakingBlock breakingBlock;
                final int id = getBlockEntityId(block);
                if (packet.getPlayerDigTypes().getValues().get(0) == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                    final PreBlockDamageEvent e;
                    final int breakTime = Breaker.getPlugin().database.get(block.getType(), block.getData());
                    if (cachedBlocks.containsKey(id)) {
                        final HashMap<String, BreakingBlock> list = cachedBlocks.get(id);
                        final String name = player.getName();
                        if (list.containsKey(name)) {
                            breakingBlock = list.get(name);
                            e             = new PreBlockDamageEvent(block, player, breakTime, breakingBlock.getStage(), breakingBlock.getTimeBroken(), breakingBlock.getLastItem());
                            Bukkit.getPluginManager().callEvent(e);
                            if (e.isCancelled()) {
                                return;
                            }
                            breakingBlock.update(e);
                        }
                        else {
                            e = new PreBlockDamageEvent(block, player, breakTime, 0, 0, player.getInventory().getItemInMainHand());
                            Bukkit.getPluginManager().callEvent(e);
                            if (e.isCancelled()) {
                                return;
                            }
                            breakingBlock = new BreakingBlock(e);
                            list.put(player.getName(), breakingBlock);
                        }
                    }
                    else {
                        e = new PreBlockDamageEvent(block, player, breakTime, 0, 0, player.getInventory().getItemInMainHand());
                        Bukkit.getPluginManager().callEvent(e);
                        if (e.isCancelled()) {
                            return;
                        }
                        final HashMap<String, BreakingBlock> list = new HashMap<>();
                        breakingBlock = new BreakingBlock(e);
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
        }).syncStart();
    }

    public static int getBlockEntityId(final Block block) {
        return (block.getX() & 0xFFF) << 20 | (block.getZ() & 0xFFF) << 8 | block.getY() & 0xFF;
    }

}

