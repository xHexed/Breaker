/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.asangarin.breaker.manager;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.core.BlockConfiguration;
import com.asangarin.breaker.utility.BreakState;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private final Collection<BreakState> states = new ArrayList<>();

    public ConfigManager() {
        for (final Class breakStates : Breaker.plugin.states.registered()) {
            try {
                states.add((BreakState)breakStates.newInstance());
            }
            catch (final IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        Breaker.plugin.database.clear();
        for (final File file : Objects.requireNonNull(new File(Breaker.plugin.getDataFolder(), "blockconfigs").listFiles())) {
            if (file.isDirectory() || !file.getName().substring(file.getName().lastIndexOf(46)).equalsIgnoreCase(".yml")) continue;
            final YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
            c.getKeys(false).forEach(entries -> {
                final BlockConfiguration block;
                final ConfigurationSection config = c.getConfigurationSection(entries);
                try {
                    block = new BlockConfiguration(config.getName().replace('-', '_'), config.getInt("min-hardness", 1), config.getInt("max-hardness", 4));
                }
                catch (final Exception ex) {
                    Breaker.warn("Couldn't load Material: " + config.getName());
                    return;
                }
                if (config.contains("states")) {
                    config.getConfigurationSection("states").getKeys(false).forEach(cstate -> {
                        BreakState breakState = null;
                        final String type = config.getString("states." + cstate + ".type");
                        if (type == null) {
                            Breaker.warn("You haven't specified a type for '" + cstate + "' (state)!");
                            return;
                        }
                        for (final BreakState state : states) {
                            if (!type.equalsIgnoreCase(state.type())) continue;
                            breakState = state;
                            break;
                        }
                        if (breakState != null) {
                            Breaker.debug("Added '" + type + "' state to: '" + block.getId() + "'", 2);
                            try {
                                block.addState(breakState.getClass().newInstance().register(config.getConfigurationSection("states." + cstate)));
                            }
                            catch (final IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Breaker.warn("Couldn't find BreakState: " + type);
                        }
                    });
                }
                Breaker.plugin.database.add(block.getId(), block);
            });
        }
    }
}

