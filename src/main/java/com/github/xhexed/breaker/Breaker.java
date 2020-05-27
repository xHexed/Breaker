package com.github.xhexed.breaker;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.xhexed.breaker.core.BreakingCore;
import com.github.xhexed.breaker.manager.ConfigManager;
import com.github.xhexed.breaker.manager.Database;
import com.github.xhexed.breaker.manager.StatesManager;
import com.github.xhexed.breaker.manager.SystemManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Breaker extends JavaPlugin {
    public static Breaker plugin;
    Settings settings;
    public ProtocolManager protocol;
    public StatesManager states;
    public Database database;
    public SystemManager system;
    private ConfigManager config;
    public BreakingCore core;

    public void onEnable() {
        plugin = this;
        if (!new File(getDataFolder(), "config.yml").exists()) {
            final File folder = new File(getDataFolder(), "/blockconfigs/");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final File sampleFile1 = new File(getDataFolder(), "/blockconfigs/STONE_BLOCKS.yml");
            final File sampleFile2 = new File(getDataFolder(), "/blockconfigs/WOODEN_BLOCKS.yml");
            if (sampleFile1.exists()) {
                return;
            }
            if (sampleFile2.exists()) {
                return;
            }
            final InputStream input1 = getClass().getResourceAsStream("/default/blockconfigs/STONE_BLOCKS.yml");
            final InputStream input2 = getClass().getResourceAsStream("/default/blockconfigs/WOODEN_BLOCKS.yml");
            final String outputFile1 = sampleFile1.getAbsolutePath();
            final String outputFile2 = sampleFile2.getAbsolutePath();
            try {
                Files.copy(input1, Paths.get(outputFile1));
                Files.copy(input2, Paths.get(outputFile2));
            }
            catch (final IOException e1) {
                e1.printStackTrace();
            }
        }
        saveDefaultConfig();
        settings = new Settings(getConfig().getConfigurationSection("settings"));
        getCommand("breaker").setExecutor(new BreakerCommand());
        getCommand("breaker").setTabCompleter(new BreakerCommand());
        protocol = ProtocolLibrary.getProtocolManager();
        states   = new StatesManager();
        database = new Database();
        system   = new SystemManager();
        config   = new ConfigManager();
        core     = new BreakingCore();
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        onReload();
    }

    void onReload() {
        reloadConfig();
        config.reload();
    }

    public static void warn(final String m) {
        plugin.getLogger().warning(m);
    }

    public static void debug(final String m, final int depth) {
        if (plugin.getConfig().getBoolean("debug.enabled") && plugin.getConfig().getInt("debug.depth") >= depth) {
            plugin.getLogger().info("[Debug] " + m);
        }
    }
}

