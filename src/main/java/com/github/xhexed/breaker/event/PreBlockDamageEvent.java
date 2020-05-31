package com.github.xhexed.breaker.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class PreBlockDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final Player player;
    private int stage;
    private boolean cancelled;
    private int breakTime;
    private int timeBroken;

    public PreBlockDamageEvent(final Block block, final Player player, final int breakTime, final int stage, final int timeBroken) {
        this.block      = block;
        this.player     = player;
        this.breakTime  = breakTime;
        this.stage      = stage;
        this.timeBroken = timeBroken;
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

    public void setStage(final int stage) {
        this.stage = stage;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(final int breakTime) {
        this.breakTime = breakTime;
    }

    public int getTimeBroken() {
        return timeBroken;
    }

    public void setTimeBroken(final int timeBroken) {
        this.timeBroken = timeBroken;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
