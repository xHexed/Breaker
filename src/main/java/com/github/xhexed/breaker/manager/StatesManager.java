package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.states.ToolState;
import com.github.xhexed.breaker.states.WorldState;
import com.github.xhexed.breaker.utility.BreakState;
import com.github.xhexed.breaker.utility.Manager;

public class StatesManager extends Manager<BreakState> {
    @Override
    public void load() {
        register(ToolState.class);
        register(WorldState.class);
    }
}

