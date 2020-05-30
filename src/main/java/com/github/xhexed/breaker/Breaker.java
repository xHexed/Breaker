package com.github.xhexed.breaker;

import com.github.xhexed.breaker.core.BlockConfiguration;
import com.github.xhexed.breaker.core.BreakingCore;
import com.github.xhexed.breaker.manager.Database;
import org.bukkit.Bukkit;
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
        plugin.database.clear();
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "blocks.yml"));
        for (final String entries : config.getKeys(false)) {
            final ConfigurationSection section = config.getConfigurationSection(entries);
            final BlockConfiguration block = new BlockConfiguration(section.getName(), section.getInt("hardness", 1));
            plugin.database.add(block.getId(), block);
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

