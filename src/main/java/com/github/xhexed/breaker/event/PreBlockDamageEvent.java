package com.github.xhexed.breaker.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class PreBlockDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final Player player;
    private final int stage;
    private boolean cancelled;
    private int breakTime;
    private int timeBroken;
    private final ItemStack lastItem;

    public PreBlockDamageEvent(final Block block, final Player player, final int breakTime, final int stage, final int timeBroken, final ItemStack lastItem) {
        this.block      = block;
        this.player     = player;
        this.breakTime  = breakTime;
        this.stage      = stage;
        this.timeBroken = timeBroken;
        this.lastItem   = lastItem;
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

    public ItemStack getLastItem() {
        return lastItem;
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
