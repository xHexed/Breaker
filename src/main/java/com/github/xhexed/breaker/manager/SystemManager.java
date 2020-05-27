package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.system.MaterialSystem;
import com.github.xhexed.breaker.utility.BreakerSystem;
import com.github.xhexed.breaker.utility.Manager;

public class SystemManager extends Manager<BreakerSystem> {
    @Override
    public void load() {
        register(MaterialSystem.class);
    }
}

