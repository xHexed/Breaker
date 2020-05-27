package com.github.xhexed.breaker;

import org.bukkit.configuration.ConfigurationSection;

public class Settings {
    private final boolean permaMF;

    Settings(final ConfigurationSection config) {
        permaMF = config.getBoolean("permanent-mining-fatigue");
    }

    public static Settings instance() {
        return Breaker.plugin.settings;
    }

    public boolean permanentMiningFatigue() {
        return permaMF;
    }
}

