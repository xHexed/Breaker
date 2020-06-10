package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.Breaker;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final Map<ImmutablePair<Material, Byte>, Integer> blockConfigs = new HashMap<>();

    public static void add(final Material material, final byte id, final int num) {
        Breaker.debug("Added " + material + ":" + id + " to the Database!");
        blockConfigs.put(new ImmutablePair<>(material, id), num);
    }

    public static int get(final Material material, final byte id) {
        return blockConfigs.get(new ImmutablePair<>(material, id));
    }

    public static void clear() {
        Breaker.debug("Cleared Database!");
        blockConfigs.clear();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean has(final Material material, final byte id) {
        return blockConfigs.containsKey(new ImmutablePair<>(material, id));
    }
}

