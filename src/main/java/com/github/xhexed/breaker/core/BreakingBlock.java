package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.event.BlockStageChangeEvent;
import com.github.xhexed.breaker.event.PreBlockBreakEvent;
import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import net.minecraft.server.v1_11_R1.BlockPosition;
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
import static com.github.xhexed.breaker.utility.NMSHandler.*;

class BreakingBlock {
    private final BlockPosition blockPosition;
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
        blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        breaker = event.getPlayer();
        breakTime = event.getBreakTime();
        timeBroken = event.getTimeBroken();
    }

    void start() {
        task = new BukkitRunnable() {
            public void run() {
                stage = timeBroken * 10 / breakTime;
                if (stage != lastStage) {
                    final BlockStageChangeEvent event = new BlockStageChangeEvent(block, breaker, stage, timeBroken, breakTime);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        BreakingBlock.this.cancel();
                    }
                }
                lastStage = stage;
                breakAnimation(stage, block, breaker);
                timeBroken++;
                if (timeBroken > breakTime) {
                    finish();
                }
            }
        };
        task.runTaskTimerAsynchronously(getPlugin(), 0, 1);

        if (cancelTask != null) {
            cancelTask.cancel();
            cancelTask = null;
        }
    }

    void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        cancelTask = new BukkitRunnable() {
            @Override
            public void run() {
                cachedBlocks.remove(blockPosition.asLong());
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

        final HashMap<String, BreakingBlock> list = cachedBlocks.remove(blockPosition.asLong());
        if (list == null) return;
        list.forEach((name, breakingBlock) -> {
            breakingBlock.cancel();
            breakAnimation(10, block, Bukkit.getPlayer(name));
        });
    }

    void update(final PreBlockDamageEvent event) {
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

