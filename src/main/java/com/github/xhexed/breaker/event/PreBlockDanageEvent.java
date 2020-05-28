package com.github.xhexed.breaker.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class PreBlockDanageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final Player player;
    private int stage = 10;
    private boolean cancelled;

    public PreBlockDanageEvent(final Block block, final Player player) {
        this.block = block;
        this.player = player;
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
