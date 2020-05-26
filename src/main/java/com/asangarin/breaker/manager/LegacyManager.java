/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.material.MaterialData
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.asangarin.breaker.manager;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LegacyManager {
    public PotionEffect getPotionEffect(final int amount) {
        return new PotionEffect(PotionEffectType.SLOW_DIGGING, amount, -1, false, false);
    }

    public void spawnBlockParticle(final Block block) {
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4.0, new MaterialData(block.getType()));
    }
}

