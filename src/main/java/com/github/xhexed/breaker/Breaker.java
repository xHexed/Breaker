package com.github.xhexed.breaker;

import com.github.xhexed.breaker.core.BreakingCore;
import com.github.xhexed.breaker.manager.Database;
import com.github.xhexed.breaker.utility.NMSHandler;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Breaker extends JavaPlugin {
    private static Breaker plugin;
    public Database database;
    public BreakingCore core;

    public void onEnable() {
        plugin = this;
        Bukkit.getScheduler().runTaskAsynchronously(this, NMSHandler::cacheSound);
        saveDefaultConfig();
        if (!new File(getDataFolder(), "blocks.yml").exists()) {
            saveResource("blocks.yml", false);
        }
        getCommand("breaker").setExecutor(new BreakerCommand());
        getCommand("breaker").setTabCompleter(new BreakerCommand());
        database = new Database();
        core     = new BreakingCore();
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        onReload();
    }

    void onReload() {
        reloadConfig();
        database.clear();
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "blocks.yml"));
        for (final String entries : config.getKeys(false)) {
            final ConfigurationSection section = config.getConfigurationSection(entries);
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
                                    section.getInt("hardness", 1));
            }
            catch (final IllegalArgumentException e) {
                getLogger().warning("Couldn't load block " + entries + "!");
            }
        }
    }

    public static void debug(final String m, final int depth) {
        if (plugin.getConfig().getBoolean("debug.enabled") && plugin.getConfig().getInt("debug.depth") >= depth) {
            plugin.getLogger().info("[Debug] " + m);
        }
    }

    public static Breaker getPlugin() {
        return plugin;
    }
}

