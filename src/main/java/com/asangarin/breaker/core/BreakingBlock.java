/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolManager
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.reflect.StructureModifier
 *  com.comphenix.protocol.wrappers.BlockPosition
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.asangarin.breaker.core;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.Settings;
import com.asangarin.breaker.core.BlockConfiguration;
import com.asangarin.breaker.core.BreakingCore;
import com.asangarin.breaker.utility.BreakState;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BreakingBlock {
    private final Block block;
    private final Player breaker;
    private final BlockConfiguration blockConfig;
    private BukkitRunnable runnable;

    public BreakingBlock(final String id, final Block block, final Player breaker) {
        blockConfig = Breaker.plugin.database.get(id);
        this.block  = block;
        this.breaker = breaker;
    }

    public void start() {
        runnable = new BukkitRunnable(){
            int stage;

            public void run() {
                final PacketContainer customAnimation = Breaker.plugin.protocol.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
                customAnimation.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
                customAnimation.getIntegers().write(0, BreakingCore.getBlockEntityId(block));
                customAnimation.getIntegers().write(1, stage);
                try {
                    Breaker.plugin.protocol.sendServerPacket(breaker, customAnimation);
                }
                catch (final InvocationTargetException e) {
                    throw new RuntimeException("Cannot send packet " + customAnimation, e);
                }
                ++stage;
                if (stage > 10) {
                    breaker.playSound(block.getLocation(), Breaker.plugin.nms.getBlockBreakSound(block), 1.0f, 1.0f);
                    Breaker.plugin.legacy.spawnBlockParticle(block);
                    Breaker.plugin.nms.breakBlock(breaker, block.getLocation());
                    finish();
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(Breaker.plugin, 0L, calculateBreakTime() / 10);
    }

    public void cancel() {
        if (runnable != null) {
            runnable.cancel();
        }
        finish();
    }

    private void finish() {
        final PacketContainer cancelBlockBreak = Breaker.plugin.protocol.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        if (cancelBlockBreak == null) {
            return;
        }
        cancelBlockBreak.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
        cancelBlockBreak.getIntegers().write(0, BreakingCore.getBlockEntityId(block));
        cancelBlockBreak.getIntegers().write(1, 10);
        try {
            Breaker.plugin.protocol.sendServerPacket(breaker, cancelBlockBreak);
        }
        catch (final InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet " + cancelBlockBreak, e);
        }
        if (breaker.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            new BukkitRunnable(){

                public void run() {
                    if (!Settings.instance().permanentMiningFatigue()) {
                        breaker.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                    }
                }
            }.runTask(Breaker.plugin);
        }
    }

    public BlockConfiguration getBlockConfiguration() {
        return blockConfig;
    }

    public Block getBlock() {
        return block;
    }

    public Player getBreaker() {
        return breaker;
    }

    private int calculateBreakTime() {
        int breakingTime = blockConfig.getMaxHardness();
        for (final BreakState state : blockConfig.getStates()) {
            Breaker.debug("Found state! " + state, 20);
            if (!state.activeState(this)) continue;
            breakingTime -= state.getStateValue(this);
        }
        breakingTime = Math.max(breakingTime, blockConfig.getMinHardness());
        Breaker.debug("Breaking Time: " + breakingTime, 1);
        return breakingTime;
    }

}

