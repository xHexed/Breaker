package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.event.PreBlockBreakEvent;
import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.xhexed.breaker.utility.NMSHandler.*;

class BreakingBlock {
    private final Block block;
    private final Player breaker;
    private int stage;
    private int timeBroken;
    private BukkitRunnable task;
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
        task = new BukkitRunnable() {
            public void run() {
                breakAnimation(timeBroken * 10 / breakTime, block, breaker);
                timeBroken++;
                if (timeBroken > breakTime) {
                    finish();
                }
            }
        };
        task.runTaskTimer(Breaker.getPlugin(), 0, 1);
    }

    void cancel() {
        task.cancel();
    }

    private void finish() {
        final PreBlockBreakEvent event = new PreBlockBreakEvent(block, breaker);
        Bukkit.getPluginManager().callEvent(event);
        task.cancel();

        if (event.getStage() < 10) {
            stage = event.getStage();
            start();
        }

        if (!event.isCancelled()) {
            breaker.playSound(block.getLocation(), getBlockBreakSound(block), 1.0f, 1.0f);
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4.0, new MaterialData(block.getType()));
            breakBlock(breaker, block.getLocation());
            Breaker.getPlugin().core.cachedBlocks.remove(BreakingCore.getBlockEntityId(block));
        }
    }

    int getStage() {
        return stage;
    }

    void setStage(final int stage) {
        this.stage = stage;
    }

    void setBreakTime(final int breakTime) {
        this.breakTime = breakTime;
        timeBroken = stage * breakTime / 10;
    }

    ItemStack getLastItem() {
        return lastItem;
    }

    void setLastItem(final ItemStack lastItem) {
        this.lastItem = lastItem;
    }

    int getTimeBroken() {
        return timeBroken;
    }
}

