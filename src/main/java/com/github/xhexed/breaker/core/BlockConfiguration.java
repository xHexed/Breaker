package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.utility.BreakState;
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

    List<BreakState> getStates() {
        return states;
    }

    public String getId() {
        return id;
    }

    int getMinHardness() {
        return minHardness;
    }

    int getMaxHardness() {
        return maxHardness;
    }
}

