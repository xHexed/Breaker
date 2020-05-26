/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.util.StringUtil
 */
package com.asangarin.breaker.command;

import java.util.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class BreakerTabComplete
implements TabCompleter {
    private static final List<String> FIRST_ARGS = Collections.singletonList("reload");

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final ArrayList<String> completions = new ArrayList<>();
        if (args == null || args.length == 0) {
            StringUtil.copyPartialMatches(Objects.requireNonNull(args)[0], FIRST_ARGS, completions);
        } else {
            switch (args.length) {
                default: {
                    break;
                }
                case 1: {
                    StringUtil.copyPartialMatches(args[0], FIRST_ARGS, completions);
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}

