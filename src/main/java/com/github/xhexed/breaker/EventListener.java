package com.github.xhexed.breaker;

import com.github.xhexed.breaker.utility.BreakerSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class EventListener implements Listener {
    @EventHandler
    public static void playerJoin(final PlayerJoinEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true);
    }

    @EventHandler
    public static void playerRespawn(final PlayerRespawnEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true);
    }

    @EventHandler
    public static void blockDamage(final BlockDamageEvent e) {
        Breaker.debug("BlockDamageEvent: " + Breaker.getPlugin().core.contains(e.getBlock()), 5);
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true);
        for (final BreakerSystem s : Breaker.getPlugin().core.getActiveSystems()) {
            if (!Breaker.getPlugin().database.has(s.getId(e.getBlock()))) continue;
            if (!e.getInstaBreak()) continue;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void playerConsumeItem(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(Breaker.getPlugin(), () -> e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true), 2L);
    }

    @EventHandler
    public void c(final BlockBreakEvent e) {
        Breaker.debug("BlockBreakEvent: " + Breaker.getPlugin().core.contains(e.getBlock()), 5);
    }
}

