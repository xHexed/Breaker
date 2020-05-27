package com.github.xhexed.breaker.system;

import com.github.xhexed.breaker.utility.BreakerSystem;
import org.bukkit.block.Block;

public class MaterialSystem implements BreakerSystem {
    @Override
    public String getId(final Block block) {
        return block.getType().name();
    }

    @Override
    public int priority() {
        return 5;
    }
}

