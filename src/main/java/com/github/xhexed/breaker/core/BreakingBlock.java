package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.event.BlockStageChangeEvent;
import com.github.xhexed.breaker.event.PreBlockBreakEvent;
import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static com.github.xhexed.breaker.Breaker.getPlugin;
import static com.github.xhexed.breaker.core.BreakingCore.cachedBlocks;
import static com.github.xhexed.breaker.core.BreakingCore.getBlockEntityId;
import static com.github.xhexed.breaker.utility.NMSHandler.*;

class BreakingBlock {
    private final Block block;
    private final Player breaker;
    private int stage;
    private int lastStage;
    private int timeBroken;
    private BukkitRunnable task;
    private BukkitRunnable cancelTask;
    private int breakTime;
    private ItemStack lastItem;

    BreakingBlock(final PreBlockDamageEvent event) {
        block  = event.getBlock();
        breaker = event.getPlayer();
        stage = event.getStage();
        breakTime = event.getBreakTime();
        timeBroken = stage * breakTime / 10;
    }

    void start() {
        if (cancelTask != null) {
            cancelTask.cancel();
            cancelTask = null;
        }
        task = new BukkitRunnable() {
            public void run() {
                stage = timeBroken * 10 / breakTime;
                if (stage != lastStage) {
                    Bukkit.getPluginManager().callEvent(new BlockStageChangeEvent(block, breaker, stage, timeBroken, breakTime));
                }
                lastStage = stage;
                breakAnimation(stage, block, breaker);
                timeBroken++;
                if (timeBroken > breakTime) {
                    finish();
                }
            }
        };
        task.runTaskTimer(getPlugin(), 0, 1);
    }

    void cancel() {
        task.cancel();
        cancelTask = new BukkitRunnable() {
            @Override
            public void run() {
                cachedBlocks.remove(getBlockEntityId(block));
            }
        };
        cancelTask.runTaskLater(getPlugin(), 400);
    }

    private void finish() {
        final PreBlockBreakEvent event = new PreBlockBreakEvent(block, breaker);
        Bukkit.getPluginManager().callEvent(event);
        task.cancel();

        if (event.getStage() < 10 && event.getStage() > 0) {
            final PreBlockDamageEvent e = new PreBlockDamageEvent(block, breaker, breakTime, event.getStage(), 0, breaker.getInventory().getItemInMainHand());
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                return;
            }
            update(e);
            start();
        }

        if (event.isCancelled())
            return;

        if (event.isPlaySound())
            breaker.playSound(block.getLocation(), getBlockBreakSound(block), 1.0f, 1.0f);
        if (event.isSpawnParticle())
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5),
                    100, 0.1, 0.1, 0.1, 4.0,
                    new MaterialData(block.getType()));
        if (event.isBreakBlock())
            breakBlock(breaker, block.getLocation());
        final HashMap<String, BreakingBlock> list = cachedBlocks.remove(getBlockEntityId(block));
        if (list == null) return;
        list.forEach((name, breakingBlock) -> {
            breakingBlock.cancel();
            breakAnimation(10, block, Bukkit.getPlayer(name));
        });
    }

    void update(final PreBlockDamageEvent event) {
        stage = event.getStage();
        timeBroken = event.getTimeBroken();
        breakTime = event.getBreakTime();
        lastItem = event.getLastItem();
    }

    int getStage() {
        return stage;
    }

    ItemStack getLastItem() {
        return lastItem;
    }

    int getTimeBroken() {
        return timeBroken;
    }
}

