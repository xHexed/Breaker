package com.github.xhexed.breaker;

import com.github.xhexed.breaker.manager.Database;
import com.github.xhexed.breaker.manager.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.github.xhexed.breaker.Breaker.getPlugin;

class EventListener implements Listener {
    private static final PotionEffect SLOWDIGGING_EFFECT = new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false);

    @EventHandler
    public static void playerJoin(final PlayerJoinEvent e) {
        e.getPlayer().addPotionEffect(SLOWDIGGING_EFFECT, true);
        PacketManager.addPlayer(e.getPlayer());
    }

    @EventHandler
    public static void playerRespawn(final PlayerRespawnEvent e) {
        e.getPlayer().addPotionEffect(SLOWDIGGING_EFFECT, true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public static void blockDamage(final BlockDamageEvent e) {
        e.getPlayer().addPotionEffect(SLOWDIGGING_EFFECT, true);
        final Block block = e.getBlock();
        if (!Database.has(block.getType(), block.getData()) || !e.getInstaBreak()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public static void playerConsumeItem(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> e.getPlayer().addPotionEffect(SLOWDIGGING_EFFECT, true), 2L);
    }
}

