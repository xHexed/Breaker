/*
 * Decompiled with CFR 0.145.
 */
package com.asangarin.breaker.core;

import com.asangarin.breaker.utility.BreakState;
import java.util.ArrayList;
import java.util.List;

public class BlockConfiguration {
    private final String id;
    private final int minHardness;
    private final int maxHardness;
    private final List<BreakState> states = new ArrayList<>();

    public BlockConfiguration(final String i, final int minH, final int maxH) {
        id          = i;
        minHardness = minH;
        maxHardness = maxH;
    }

    public void addState(final BreakState state) {
        states.add(state);
    }

    public List<BreakState> getStates() {
        return states;
    }

    public String getId() {
        return id;
    }

    public int getMinHardness() {
        return minHardness;
    }

    public int getMaxHardness() {
        return maxHardness;
    }
}

