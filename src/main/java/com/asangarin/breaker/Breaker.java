/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.ProtocolManager
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.asangarin.breaker;

import com.asangarin.breaker.command.BreakerCommand;
import com.asangarin.breaker.command.BreakerTabComplete;
import com.asangarin.breaker.core.BreakingCore;
import com.asangarin.breaker.legacy.DefaultManager;
import com.asangarin.breaker.legacy.LegacyManager;
import com.asangarin.breaker.legacy.VersionWrapper;
import com.asangarin.breaker.manager.ConfigManager;
import com.asangarin.breaker.manager.Database;
import com.asangarin.breaker.manager.StatesManager;
import com.asangarin.breaker.manager.SystemManager;
import com.asangarin.breaker.utility.NMSHandler;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Breaker
extends JavaPlugin {
    private static final Pattern DOT = Pattern.compile(".", Pattern.LITERAL);
    public static Breaker plugin;
    Settings settings;
    public ProtocolManager protocol;
    public StatesManager states;
    public Database database;
    public SystemManager system;
    public NMSHandler nms;
    public VersionWrapper legacy;
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
            final InputStream input1 = ((Object) this).getClass().getResourceAsStream("/default/blockconfigs/STONE_BLOCKS.yml");
            final InputStream input2 = ((Object) this).getClass().getResourceAsStream("/default/blockconfigs/WOODEN_BLOCKS.yml");
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
        getCommand("breaker").setTabCompleter(new BreakerTabComplete());
        protocol = ProtocolLibrary.getProtocolManager();
        try {
            nms = (NMSHandler)Class.forName("com.asangarin.breaker.nms.NMSHandler_" + DOT.matcher(Bukkit.getServer().getClass().getPackage().getName()).replaceAll(Matcher.quoteReplacement(",")).split(",")[3].substring(1)).newInstance();
        }
        catch (final ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Breaker.warn("Couldn't find NMS class for version: '" + DOT.matcher(Bukkit.getServer().getClass().getPackage().getName()).replaceAll(Matcher.quoteReplacement(",")).split(",")[3].substring(1) + "'!");
            Breaker.warn("Are you using a supported version of Minecraft?");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        legacy   = nms.getVersion().equals("1.12_R1") ? new LegacyManager() : new DefaultManager();
        states   = new StatesManager();
        database = new Database();
        system   = new SystemManager();
        config   = new ConfigManager();
        core     = new BreakingCore();
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        onReload();
    }

    public void onReload() {
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

