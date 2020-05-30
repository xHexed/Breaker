package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.Breaker;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.Map;

public class Database {
    private final Map<Material, Integer> blockConfigs = new EnumMap<>(Material.class);

    public void add(final Material mat, final Integer num) {
        Breaker.debug("Added " + mat + " to the Database!", 3);
        blockConfigs.put(mat, num);
    }

    public int get(final Material material) {
        return blockConfigs.get(material);
    }

    public void clear() {
        Breaker.debug("Cleared Database!", 4);
        blockConfigs.clear();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean has(final Material material) {
        Breaker.debug("Checking if " + material + " exists in Database... (" + blockConfigs.containsKey(material) + ")", 3);
        return blockConfigs.containsKey(material);
    }
}

