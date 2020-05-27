package com.github.xhexed.breaker.manager;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LegacyManager {
    public static PotionEffect getPotionEffect(final int amount) {
        return new PotionEffect(PotionEffectType.SLOW_DIGGING, amount, -1, false, false);
    }

    public static void spawnBlockParticle(final Block block) {
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4.0, new MaterialData(block.getType()));
    }
}

