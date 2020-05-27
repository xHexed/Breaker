package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.utility.BreakerSystem;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.xhexed.breaker.utility.NMSHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class BreakingCore {
    private final Map<Integer, BreakingBlock> cachedBlocks = new HashMap<>();
    private final List<Material> excludedMaterials;
    private List<BreakerSystem> systems;

    public BreakingCore() {
        excludedMaterials = NMSHandler.getExlcudedBlocks();
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
                if (digType != EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                    event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
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

