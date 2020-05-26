/*
 * Decompiled with CFR 0.145.
 */
package com.asangarin.breaker.manager;

import com.asangarin.breaker.states.ToolState;
import com.asangarin.breaker.states.WorldState;
import com.asangarin.breaker.utility.BreakState;
import com.asangarin.breaker.utility.Manager;

public class StatesManager
extends Manager<BreakState> {
    @Override
    public void load() {
        register(ToolState.class);
        register(WorldState.class);
    }
}

