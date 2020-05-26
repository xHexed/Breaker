/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 */
package com.asangarin.breaker;

import com.asangarin.breaker.Breaker;
import org.bukkit.configuration.ConfigurationSection;

public class Settings {
    private final boolean permaMF;

    public Settings(final ConfigurationSection config) {
        permaMF = config.getBoolean("permanent-mining-fatigue");
    }

    public static Settings instance() {
        return Breaker.plugin.settings;
    }

    public boolean permanentMiningFatigue() {
        return permaMF;
    }
}

