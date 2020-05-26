/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.ConfigurationSection
 */
package com.asangarin.breaker.states;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BreakingBlock;
import com.asangarin.breaker.utility.BreakState;
import org.bukkit.configuration.ConfigurationSection;

public class WorldState
implements BreakState {
    private int value;
    private String worldname;

    private WorldState register(final String wn, final int val) {
        worldname = wn;
        value     = val;
        return this;
    }

    @Override
    public String type() {
        return "world";
    }

    @Override
    public boolean activeState(final BreakingBlock block) {
        Breaker.debug("World Test: " + block.getBlock().getWorld().getName().toLowerCase() + " | " + worldname, 6);
        return block.getBlock().getWorld().getName().equalsIgnoreCase(worldname);
    }

    @Override
    public int getStateValue(final BreakingBlock block) {
        return value;
    }

    @Override
    public BreakState register(final ConfigurationSection c) {
        return register(c.getString("world", "world").toLowerCase(), c.getInt("hardness", 1));
    }
}

