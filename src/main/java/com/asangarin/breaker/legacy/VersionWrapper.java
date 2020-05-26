/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.potion.PotionEffect
 */
package com.asangarin.breaker.legacy;

import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;

public abstract class VersionWrapper {
    public abstract PotionEffect getPotionEffect(int var1);

    public abstract void spawnBlockParticle(Block var1);
}

