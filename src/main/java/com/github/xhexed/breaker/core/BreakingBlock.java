package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.event.PreBlockBreakEvent;
import com.github.xhexed.breaker.event.PreBlockDamageEvent;
import com.github.xhexed.breaker.utility.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

class BreakingBlock {
    private final Block block;
    private final Player breaker;
    private int stage;
    private BukkitRunnable task;
    private final int breakTime;

    BreakingBlock(final PreBlockDamageEvent event) {
        block  = event.getBlock();
        breaker = event.getPlayer();
        stage = event.getStage();
        breakTime = event.getBreakTime();
    }

    void start() {
        task = new BukkitRunnable() {
            public void run() {
                NMSHandler.breakAnimation(stage, block, breaker);
                stage++;
                if (stage > 10) {
                    finish();
                }
            }
        };
        task.runTaskTimer(Breaker.getPlugin(), 0L, breakTime / 10);
    }

    void cancel() {
        task.cancel();
    }

    private void finish() {
        final PreBlockBreakEvent event = new PreBlockBreakEvent(block, breaker);
        Bukkit.getPluginManager().callEvent(event);
        task.cancel();

        if (event.getStage() != 10) {
            stage = event.getStage();
            start();
        }

        if (!event.isCancelled()) {
            breaker.playSound(block.getLocation(), NMSHandler.getBlockBreakSound(block), 1.0f, 1.0f);
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4.0, new MaterialData(block.getType()));
            NMSHandler.breakBlock(breaker, block.getLocation());
            Breaker.getPlugin().core.cachedBlocks.remove(BreakingCore.getBlockEntityId(block));
        }
    }
}

