/*
 * Decompiled with CFR 0.145.
 */
package com.asangarin.breaker.manager;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BlockConfiguration;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<String, BlockConfiguration> blockConfigs = new HashMap<>();

    public void add(final String config, final BlockConfiguration block) {
        Breaker.debug("Added " + config.toLowerCase() + " to the Database!", 3);
        blockConfigs.put(config.toLowerCase(), block);
    }

    public BlockConfiguration get(final String config) {
        Breaker.debug("Retrieving " + config.toLowerCase() + " from the Database!", 3);
        return blockConfigs.get(config.toLowerCase());
    }

    void clear() {
        Breaker.debug("Cleared Database!", 4);
        blockConfigs.clear();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean has(final String config) {
        Breaker.debug("Checking if " + config.toLowerCase() + " exists in Database... (" + blockConfigs.containsKey(config.toLowerCase()) + ")", 3);
        return blockConfigs.containsKey(config.toLowerCase());
    }
}

