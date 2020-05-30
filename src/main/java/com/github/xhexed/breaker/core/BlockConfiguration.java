package com.github.xhexed.breaker.core;

public class BlockConfiguration {
    private final String id;
    private final int hardness;

    public BlockConfiguration(final String id, final int hardness) {
        this.id       = id;
        this.hardness = hardness;
    }

    public String getId() {
        return id;
    }

    int getHardness() {
        return hardness;
    }
}

