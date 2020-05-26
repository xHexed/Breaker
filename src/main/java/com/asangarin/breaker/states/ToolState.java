/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.asangarin.breaker.states;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BreakingBlock;
import com.asangarin.breaker.utility.BreakState;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class ToolState
implements BreakState {
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

