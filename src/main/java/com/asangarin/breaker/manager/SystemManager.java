/*
 * Decompiled with CFR 0.145.
 */
package com.asangarin.breaker.manager;

import com.asangarin.breaker.system.MaterialSystem;
import com.asangarin.breaker.utility.BreakerSystem;
import com.asangarin.breaker.utility.Manager;

public class SystemManager
extends Manager<BreakerSystem> {
    @Override
    public void load() {
        register(MaterialSystem.class);
    }
}

