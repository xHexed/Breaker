package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.event.PreBlockBreakEvent;
import com.github.xhexed.breaker.utility.BreakState;
import com.github.xhexed.breaker.utility.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class BreakingBlock {
    private final Block block;
    private final Player breaker;
    private final BlockConfiguration blockConfig;
    private int stage;
    private BukkitRunnable task;

    BreakingBlock(final String id, final Block block, final Player breaker, final int stage) {
        blockConfig = Breaker.getPlugin().database.get(id);
        this.block  = block;
        this.breaker = breaker;
        this.stage = stage;
    }

    void start() {
        task = new BukkitRunnable() {
            public void run() {
                NMSHandler.breakAnimation(stage, block, breaker);
                stage++;
                if (stage > 10) {
                    finish();
                    cancel();
                }
            }
        };
        task.runTaskTimer(Breaker.getPlugin(), 0L, calculateBreakTime() / 10);
    }

    void cancel() {
        if (task == null) {
            return;
        }
        task.cancel();
    }

    private void finish() {
        final PreBlockBreakEvent event = new PreBlockBreakEvent(block, breaker);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            NMSHandler.breakAnimation(event.getStage(), block, breaker);
        }
        else {
            breaker.playSound(block.getLocation(), NMSHandler.getBlockBreakSound(block), 1.0f, 1.0f);
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 100, 0.1, 0.1, 0.1, 4.0, new MaterialData(block.getType()));
            NMSHandler.breakBlock(breaker, block.getLocation());
            NMSHandler.breakAnimation(10, block, breaker);
            final int blockId = BreakingCore.getBlockEntityId(block);
            Breaker.getPlugin().core.cachedBlocks.remove(blockId);
        }
    }

    public Block getBlock() {
        return block;
    }

    public Player getBreaker() {
        return breaker;
    }

    private int calculateBreakTime() {
        int breakingTime = blockConfig.getMaxHardness();
        for (final BreakState state : blockConfig.getStates()) {
            Breaker.debug("Found state! " + state, 20);
            if (!state.activeState(this)) continue;
            breakingTime -= state.getStateValue(this);
        }
        breakingTime = Math.max(breakingTime, blockConfig.getMinHardness());
        Breaker.debug("Breaking Time: " + breakingTime, 1);
        return breakingTime;
    }

}

