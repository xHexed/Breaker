/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 */
package com.asangarin.breaker.utility;

import com.asangarin.breaker.core.BreakingBlock;
import org.bukkit.configuration.ConfigurationSection;

public interface BreakState {
    String type();

    boolean activeState(BreakingBlock var1);

    int getStateValue(BreakingBlock var1);

    BreakState register(ConfigurationSection var1);
}

