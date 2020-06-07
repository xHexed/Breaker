package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.Breaker;
import javafx.util.Pair;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Pair<Material, Byte>, Integer> blockConfigs = new HashMap<>();

    public void add(final Pair<Material, Byte> mat, final int num) {
        Breaker.debug("Added " + mat + " to the Database!");
        blockConfigs.put(mat, num);
    }

    public int get(final Material material, final byte id) {
        return blockConfigs.get(new Pair<>(material, id));
    }

    public void clear() {
        Breaker.debug("Cleared Database!");
        blockConfigs.clear();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean has(final Material material, final byte id) {
        return blockConfigs.containsKey(new Pair<>(material, id));
    }
}

