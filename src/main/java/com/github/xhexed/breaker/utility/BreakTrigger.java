package com.github.xhexed.breaker.utility;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public interface BreakTrigger {
    String type();

    void execute(Player var1, Block var2);

    BreakTrigger register(ConfigurationSection var1);
}

