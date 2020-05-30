package com.github.xhexed.breaker.utility;

import org.bukkit.configuration.ConfigurationSection;

public interface BreakState {
    String type();

    BreakState register(ConfigurationSection config);
}

