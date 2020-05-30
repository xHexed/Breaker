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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.xhexed.breaker.Breaker.getPlugin;

public class BreakingCore {
    final Map<Integer, BreakingBlock> cachedBlocks = new HashMap<>();
    private static final Set<Material> excludedMaterials = EnumSet.of(Material.AIR, Material.GRASS, Material.END_ROD, Material.BARRIER, Material.TORCH, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF, Material.LONG_GRASS, Material.BEETROOT_BLOCK, Material.WHEAT, Material.POTATO, Material.CARROT, Material.SAPLING, Material.FLOWER_POT, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.DOUBLE_PLANT, Material.WATER_LILY, Material.FIRE, Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WART_BLOCK, Material.REDSTONE_WIRE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.SLIME_BLOCK, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.STRUCTURE_VOID, Material.SUGAR_CANE_BLOCK, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK);

    public BreakingCore() {
        ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(new PacketAdapter(getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                final Player player = event.getPlayer();
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                final BlockPosition bp = packet.getBlockPositionModifier().read(0);
                final Block block = player.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
                if (block == null ||
                        excludedMaterials.contains(block.getType()) ||
                        !Breaker.getPlugin().database.has(block.getType().name())
                ) return;
                final BreakingBlock breakingBlock;
                final int id = getBlockEntityId(block);
                if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                    if (cachedBlocks.containsKey(id)) {
                        cachedBlocks.get(id).start();
                    }
                    else {
                        final PreBlockDamageEvent e = new PreBlockDamageEvent(block, player, Breaker.getPlugin().database.get(block.getType().name()).getHardness());
                        Bukkit.getPluginManager().callEvent(e);
                        if (event.isCancelled()) {
                            return;
                        }
                        breakingBlock = new BreakingBlock(e);
                        cachedBlocks.put(id, breakingBlock);
                        breakingBlock.start();
                        }
                    }
                else {
                    if (cachedBlocks.containsKey(id)) {
                        breakingBlock = cachedBlocks.get(id);
                        breakingBlock.cancel();
                    }
                }
            }
        }).syncStart();
    }

    public boolean contains(final Block block) {
        return cachedBlocks.containsKey(getBlockEntityId(block));
    }

    public static int getBlockEntityId(final Block block) {
        return (block.getX() & 0xFFF) << 20 | (block.getZ() & 0xFFF) << 8 | block.getY() & 0xFF;
    }

}

