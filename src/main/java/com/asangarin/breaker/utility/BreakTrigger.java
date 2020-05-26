/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 */
package com.asangarin.breaker.utility;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface BreakTrigger {
    String type();

    void execute(Player var1, Block var2);

    BreakTrigger register(ConfigurationSection var1);
}

