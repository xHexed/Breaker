package com.github.xhexed.breaker;

import com.github.xhexed.breaker.manager.Database;
import com.github.xhexed.breaker.utility.NMSHandler;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Breaker extends JavaPlugin {
    private static Breaker plugin;
    public Database database;

    public void onEnable() {
        plugin = this;
        Bukkit.getScheduler().runTaskAsynchronously(this, NMSHandler::cacheSound);
        saveDefaultConfig();
        getCommand("breaker").setExecutor(new BreakerCommand());
        getCommand("breaker").setTabCompleter(new BreakerCommand());
        database = new Database();
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        onReload();
    }

    void onReload() {
        reloadConfig();
        database.clear();
        final FileConfiguration config = getConfig();
        for (final String entries : config.getKeys(false)) {
            try {
                final Material material;
                final byte id;
                if (entries.contains(":")) {
                    material = Material.valueOf(entries.substring(0, entries.indexOf(':')));
                    id = Byte.parseByte(entries.substring(entries.indexOf(':') + 1));
                }
                else {
                    material = Material.valueOf(entries);
                    id = 0;
                }
                database.add(new Pair<>(material, id),
                             config.getConfigurationSection(entries).getInt("hardness", 1));
            }
            catch (final IllegalArgumentException e) {
                getLogger().warning("Couldn't load block " + entries + "!");
            }
        }
    }

    public static void debug(final String m) {
        plugin.getLogger().info(m);
    }

    public static Breaker getPlugin() {
        return plugin;
    }
}

