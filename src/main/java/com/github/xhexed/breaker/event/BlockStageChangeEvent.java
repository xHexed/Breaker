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

    public BlockStageChangeEvent(final Block block, final Player player, final int stage) {
        this.block = block;
        this.player = player;
        this.stage = stage;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
