/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.asangarin.breaker.command;

import com.asangarin.breaker.Breaker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BreakerCommand
implements CommandExecutor {
    private final String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6[&4Breaker&6] ");

    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        final Object player;
        if (sender instanceof Player && !(player = sender).hasPermission("breaker.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
            return true;
        }
        if (args == null || args.length == 0) {
            sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Version " + Breaker.plugin.getDescription().getVersion());
        } else {
            switch (args[0]) {
                case "reload": {
                    Breaker.plugin.onReload();
                    sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Successfully reloaded.");
                    break;
                }
                default: {
                    sender.sendMessage(pluginPrefix + ChatColor.RED + "Unknown command.");
                }
            }
        }
        return true;
    }
}

