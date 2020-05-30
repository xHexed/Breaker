package com.github.xhexed.breaker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.github.xhexed.breaker.Breaker.*;

class EventListener implements Listener {
    @EventHandler
    public static void playerJoin(final PlayerJoinEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true);
    }

    @EventHandler
    public static void playerRespawn(final PlayerRespawnEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public static void blockDamage(final BlockDamageEvent e) {
        debug("BlockDamageEvent: " + getPlugin().core.contains(e.getBlock()), 5);
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true);
        final Block block = e.getBlock();
        if (!getPlugin().database.has(block.getType(), block.getData()) || !e.getInstaBreak()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public static void playerConsumeItem(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false), true), 2L);
    }

    @EventHandler
    public void playerBreakBlock(final BlockBreakEvent e) {
        debug("BlockBreakEvent: " + getPlugin().core.contains(e.getBlock()), 5);
    }
}

