package com.github.xhexed.breaker.utility;

import com.github.xhexed.breaker.core.BreakingBlock;
import org.bukkit.configuration.ConfigurationSection;

public interface BreakState {
    String type();

    boolean activeState(BreakingBlock var1);

    int getStateValue(BreakingBlock var1);

    BreakState register(ConfigurationSection var1);
}

