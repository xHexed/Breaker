/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.asangarin.breaker.legacy;

import com.asangarin.breaker.legacy.VersionWrapper;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DefaultManager
extends VersionWrapper {
    @Override
    public PotionEffect getPotionEffect(final int amount) {
        return new PotionEffect(PotionEffectType.SLOW_DIGGING, amount, -1, false, false, false);
    }

    @Override
    public void spawnBlockParticle(final Block block) {
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4.0, (Object)block.getBlockData());
    }
}

