/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package com.asangarin.breaker.utility;

import com.asangarin.breaker.Breaker;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class NMSHandler {
    protected NMSHandler() {
        Breaker.debug("Loaded NMS Version: " + getVersion(), 0);
    }

    public abstract String getVersion();

    public abstract void breakBlock(Player var1, Location var2);

    public abstract Sound getBlockBreakSound(Block var1);

    public abstract List<Material> getExlcudedBlocks();
}

