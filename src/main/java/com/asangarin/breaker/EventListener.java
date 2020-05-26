/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockDamageEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 */
package com.asangarin.breaker;

import com.asangarin.breaker.Breaker;
import com.asangarin.breaker.Settings;
import com.asangarin.breaker.utility.BreakerSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;

class EventListener
implements Listener {
    @EventHandler
    public void playerJoin(final PlayerJoinEvent e) {
        e.getPlayer().addPotionEffect(Breaker.plugin.legacy.getPotionEffect(Settings.instance().permanentMiningFatigue() ? Integer.MAX_VALUE : 120), true);
    }

    @EventHandler
    public void playerRespawn(final PlayerRespawnEvent e) {
        e.getPlayer().addPotionEffect(Breaker.plugin.legacy.getPotionEffect(Settings.instance().permanentMiningFatigue() ? Integer.MAX_VALUE : 120), true);
    }

    @EventHandler
    public void blockDamage(final BlockDamageEvent e) {
        Breaker.debug("BlockDamageEvent: " + Breaker.plugin.core.contains(e.getBlock()), 5);
        for (final BreakerSystem s : Breaker.plugin.core.getActiveSystems()) {
            if (!Breaker.plugin.database.has(s.getId(e.getBlock()))) continue;
            e.getPlayer().addPotionEffect(Breaker.plugin.legacy.getPotionEffect(Integer.MAX_VALUE), true);
            if (!e.getInstaBreak()) continue;
            Breaker.plugin.core.caught(e.getBlock());
            e.setCancelled(true);
            if (!e.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING)) continue;
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
    }

    @EventHandler
    public void playerConsumeItem(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        if (!Settings.instance().permanentMiningFatigue()) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(Breaker.plugin, () -> e.getPlayer().addPotionEffect(Breaker.plugin.legacy.getPotionEffect(Integer.MAX_VALUE), true), 2L);
    }

    @EventHandler
    public void c(final BlockBreakEvent e) {
        Breaker.debug("BlockBreakEvent: " + Breaker.plugin.core.contains(e.getBlock()), 5);
    }

}

