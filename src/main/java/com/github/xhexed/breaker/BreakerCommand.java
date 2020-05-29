package com.github.xhexed.breaker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BreakerCommand implements CommandExecutor, TabCompleter {
    private static final List<String> FIRST_ARGS = Collections.singletonList("reload");

    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (sender instanceof Player && !sender.hasPermission("breaker.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
            return true;
        }
        final String pluginPrefix = "§6[§4Breaker§6] ";
        if (args == null || args.length == 0) {
            sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Version " + Breaker.getPlugin().getDescription().getVersion());
        } else {
            if ("reload".equals(args[0])) {
                Breaker.getPlugin().onReload();
                sender.sendMessage(pluginPrefix + ChatColor.GREEN + "Successfully reloaded.");
            }
            else {
                sender.sendMessage(pluginPrefix + ChatColor.RED + "Unknown command.");
            }
        }
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final ArrayList<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], FIRST_ARGS, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

