package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.Breaker;
import javafx.util.Pair;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final Map<Pair<Material, Byte>, Integer> blockConfigs = new HashMap<>();

    public static void add(final Pair<Material, Byte> mat, final int num) {
        Breaker.debug("Added " + mat + " to the Database!");
        blockConfigs.put(mat, num);
    }

    public static int get(final Material material, final byte id) {
        return blockConfigs.get(new Pair<>(material, id));
    }

    public static void clear() {
        Breaker.debug("Cleared Database!");
        blockConfigs.clear();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean has(final Material material, final byte id) {
        return blockConfigs.containsKey(new Pair<>(material, id));
    }
}

