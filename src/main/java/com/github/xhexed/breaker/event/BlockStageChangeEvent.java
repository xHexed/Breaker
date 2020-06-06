package com.github.xhexed.breaker.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class BlockStageChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final int stage;
    private final Block block;
    private final Player player;
    private final int timeBroken;
    private final int breakTime;

    public BlockStageChangeEvent(final Block block, final Player player, final int stage, final int timeBroken, final int breakTime) {
        this.block      = block;
        this.player     = player;
        this.stage      = stage;
        this.timeBroken = timeBroken;
        this.breakTime  = breakTime;
    }

    public Block getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }

    public int getStage() {
        return stage;
    }

    public int getTimeBroken() {
        return timeBroken;
    }

    public int getBreakTime() {
        return breakTime;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
