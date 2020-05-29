package com.github.xhexed.breaker.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.utility.BreakerSystem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class BreakingCore {
    private final Map<Integer, BreakingBlock> cachedBlocks = new HashMap<>();
    private static final Set<Material> excludedMaterials = EnumSet.of(Material.AIR, Material.GRASS, Material.END_ROD, Material.BARRIER, Material.TORCH, Material.REDSTONE_TORCH_ON, Material.REDSTONE_TORCH_OFF, Material.LONG_GRASS, Material.BEETROOT_BLOCK, Material.WHEAT, Material.POTATO, Material.CARROT, Material.SAPLING, Material.FLOWER_POT, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.DOUBLE_PLANT, Material.WATER_LILY, Material.FIRE, Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WART_BLOCK, Material.REDSTONE_WIRE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.SLIME_BLOCK, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.STRUCTURE_VOID, Material.SUGAR_CANE_BLOCK, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK);
    private List<BreakerSystem> systems;

    public BreakingCore() {
        systems           = new ArrayList<>();
        for (final Class breakerSystems : Breaker.plugin.system.registered()) {
            try {
                systems.add((BreakerSystem)breakerSystems.newInstance());
            }
            catch (final IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        systems = systems.stream().sorted(Comparator.comparingInt(BreakerSystem::priority)).collect(Collectors.toList());
        Breaker.plugin.protocol.getAsynchronousManager().registerAsyncHandler(new PacketAdapter(Breaker.plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                if (event.getPacketType() != PacketType.Play.Client.BLOCK_DIG) {
                    return;
                }
                final Player player = event.getPlayer();
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                final BlockPosition bp = packet.getBlockPositionModifier().read(0);
                final Block block = player.getWorld().getBlockAt(bp.getX(), bp.getY(), bp.getZ());
                if (block == null) {
                    return;
                }
                if (excludedMaterials.contains(block.getType())) {
                    return;
                }
                BreakerSystem system = null;
                for (final BreakerSystem sys : systems) {
                    if (!Breaker.plugin.database.has(sys.getId(block))) continue;
                    system = sys;
                    break;
                }
                if (system != null) {
                    final BreakingBlock breakingBlock;
                    if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                        breakingBlock = new BreakingBlock(system.getId(block), block, player);
                        cachedBlocks.put(BreakingCore.getBlockEntityId(block), breakingBlock);
                        breakingBlock.start();
                    } else {
                        final int blockId = BreakingCore.getBlockEntityId(block);
                        if (cachedBlocks.containsKey(blockId)) {
                            breakingBlock = cachedBlocks.remove(blockId);
                            breakingBlock.cancel();
                        }
                    }
                }
            }
        }).syncStart();
    }

    public boolean contains(final Block block) {
        return cachedBlocks.containsKey(BreakingCore.getBlockEntityId(block));
    }

    public void caught(final Block block) {
        cachedBlocks.remove(BreakingCore.getBlockEntityId(block)).cancel();
    }

    public static int getBlockEntityId(final Block block) {
        return (block.getX() & 0xFFF) << 20 | (block.getZ() & 0xFFF) << 8 | block.getY() & 0xFF;
    }

    public List<BreakerSystem> getActiveSystems() {
        return systems;
    }

}

