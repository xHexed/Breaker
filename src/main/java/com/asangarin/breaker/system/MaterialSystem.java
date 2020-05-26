/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 */
package com.asangarin.breaker.system;

import com.asangarin.breaker.utility.BreakerSystem;
import org.bukkit.block.Block;

public class MaterialSystem
implements BreakerSystem {
    @Override
    public String getId(final Block block) {
        return block.getType().name();
    }

    @Override
    public int priority() {
        return 5;
    }
}

