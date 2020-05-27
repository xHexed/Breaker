package com.github.xhexed.breaker.utility;

import org.bukkit.block.Block;

public interface BreakerSystem {
    int priority();

    String getId(Block var1);
}

