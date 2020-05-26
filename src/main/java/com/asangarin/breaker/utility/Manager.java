/*
 * Decompiled with CFR 0.145.
 */
package com.asangarin.breaker.utility;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager<T> {
    private final List<Class<? extends T>> registered = new ArrayList<>();

    protected Manager() {
        load();
    }

    protected abstract void load();

    protected void register(final Class<? extends T> stateClass) {
        registered.add(stateClass);
    }

    public List<Class<? extends T>> registered() {
        return registered;
    }
}

