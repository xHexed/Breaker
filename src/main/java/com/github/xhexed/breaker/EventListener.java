package com.github.xhexed.breaker;

import com.github.xhexed.breaker.manager.LegacyManager;
import com.github.xhexed.breaker.utility.BreakerSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;

class EventListener implements Listener {
    @EventHandler
    public static void playerJoin(final PlayerJoinEvent e) {
        e.getPlayer().addPotionEffect(LegacyManager.getPotionEffect(Settings.instance().permanentMiningFatigue() ? Integer.MAX_VALUE : 120), true);
    }

    @EventHandler
    public static void playerRespawn(final PlayerRespawnEvent e) {
        e.getPlayer().addPotionEffect(LegacyManager.getPotionEffect(Settings.instance().permanentMiningFatigue() ? Integer.MAX_VALUE : 120), true);
    }

    @EventHandler
    public static void blockDamage(final BlockDamageEvent e) {
        Breaker.debug("BlockDamageEvent: " + Breaker.plugin.core.contains(e.getBlock()), 5);
        for (final BreakerSystem s : Breaker.plugin.core.getActiveSystems()) {
            if (!Breaker.plugin.database.has(s.getId(e.getBlock()))) continue;
            e.getPlayer().addPotionEffect(LegacyManager.getPotionEffect(Integer.MAX_VALUE), true);
            if (!e.getInstaBreak()) continue;
            Breaker.plugin.core.caught(e.getBlock());
            e.setCancelled(true);
            if (!e.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING)) continue;
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler
    public static void playerConsumeItem(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        if (!Settings.instance().permanentMiningFatigue()) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(Breaker.plugin, () -> e.getPlayer().addPotionEffect(LegacyManager.getPotionEffect(Integer.MAX_VALUE), true), 2L);
    }

    @EventHandler
    public void c(final BlockBreakEvent e) {
        Breaker.debug("BlockBreakEvent: " + Breaker.plugin.core.contains(e.getBlock()), 5);
    }
}

