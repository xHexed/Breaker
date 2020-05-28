package com.github.xhexed.breaker.states;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.core.BreakingBlock;
import com.github.xhexed.breaker.utility.BreakState;
import org.bukkit.configuration.ConfigurationSection;

public class WorldState implements BreakState {
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

