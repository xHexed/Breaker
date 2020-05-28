package com.github.xhexed.breaker.core;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.Settings;
import com.github.xhexed.breaker.manager.LegacyManager;
import com.github.xhexed.breaker.utility.BreakState;
import com.github.xhexed.breaker.utility.NMSHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BreakingBlock {
    private final Block block;
    private final Player breaker;
    private final BlockConfiguration blockConfig;
    private BukkitRunnable runnable;

    BreakingBlock(final String id, final Block block, final Player breaker) {
        blockConfig = Breaker.plugin.database.get(id);
        this.block  = block;
        this.breaker = breaker;
    }

    void start() {
        runnable = new BukkitRunnable() {
            int stage;

            public void run() {
                NMSHandler.breakAnimation(stage, block, breaker);
                ++stage;
                if (stage > 10) {
                    breaker.playSound(block.getLocation(), NMSHandler.getBlockBreakSound(block), 1.0f, 1.0f);
                    LegacyManager.spawnBlockParticle(block);
                    NMSHandler.breakBlock(breaker, block.getLocation());
                    finish();
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(Breaker.plugin, 0L, calculateBreakTime() / 10);
    }

    void cancel() {
        if (runnable != null) {
            runnable.cancel();
        }
        finish();
    }

    private void finish() {
        NMSHandler.breakAnimation(10, block, breaker);
        if (breaker.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            new BukkitRunnable(){

                public void run() {
                    if (!Settings.instance().permanentMiningFatigue()) {
                        breaker.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                    }
                }
            }.runTask(Breaker.plugin);
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

