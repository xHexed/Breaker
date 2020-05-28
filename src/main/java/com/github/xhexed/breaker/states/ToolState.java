package com.github.xhexed.breaker.states;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.core.BreakingBlock;
import com.github.xhexed.breaker.utility.BreakState;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ToolState implements BreakState {
    private Material material;
    private int value;

    private ToolState register(final Material mat, final int val) {
        material = mat;
        value    = val;
        return this;
    }

    @Override
    public String type() {
        return "tool";
    }

    @Override
    public boolean activeState(final BreakingBlock block) {
        Breaker.debug("Tool Test: " + block.getBreaker().getInventory().getItemInMainHand().getType() + " | " + material, 6);
        return block.getBreaker().getInventory().getItemInMainHand().getType() == material;
    }

    @Override
    public int getStateValue(final BreakingBlock block) {
        return value;
    }

    @Override
    public BreakState register(final ConfigurationSection c) {
        try {
            return register(Material.valueOf(c.getString("material", "AIR")), c.getInt("hardness", 1));
        }
        catch (final Exception e) {
            Breaker.warn("'" + c.getName() + "' couldn't read material of '" + c.getString("material") + "'!");
            return null;
        }
    }
}

