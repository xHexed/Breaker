/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_13_R2.Block
 *  net.minecraft.server.v1_13_R2.BlockPosition
 *  net.minecraft.server.v1_13_R2.EntityPlayer
 *  net.minecraft.server.v1_13_R2.IBlockData
 *  net.minecraft.server.v1_13_R2.MinecraftKey
 *  net.minecraft.server.v1_13_R2.PlayerInteractManager
 *  net.minecraft.server.v1_13_R2.SoundEffect
 *  net.minecraft.server.v1_13_R2.SoundEffectType
 *  net.minecraft.server.v1_13_R2.WorldServer
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_13_R2.CraftWorld
 *  org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package com.asangarin.breaker.nms;

import com.asangarin.breaker.utility.NMSHandler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffectType;
import net.minecraft.server.v1_13_R2.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandler_1_13_R2
extends NMSHandler {
    private final List<Material> excludedMaterials = new ArrayList<>(Arrays.asList(Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.END_ROD, Material.BARRIER, Material.BRAIN_CORAL, Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL, Material.FIRE_CORAL_FAN, Material.HORN_CORAL, Material.HORN_CORAL_FAN, Material.TUBE_CORAL, Material.TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL, Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL, Material.DEAD_HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL, Material.DEAD_TUBE_CORAL_FAN, Material.TORCH, Material.REDSTONE_TORCH, Material.WALL_TORCH, Material.REDSTONE_WALL_TORCH, Material.FERN, Material.LARGE_FERN, Material.BEETROOTS, Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.OAK_SAPLING, Material.DARK_OAK_SAPLING, Material.SPRUCE_SAPLING, Material.ACACIA_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING, Material.FLOWER_POT, Material.POPPY, Material.DANDELION, Material.ALLIUM, Material.BLUE_ORCHID, Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.LILY_PAD, Material.FIRE, Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WART, Material.REDSTONE_WIRE, Material.COMPARATOR, Material.REPEATER, Material.SLIME_BLOCK, Material.STRUCTURE_VOID, Material.SUGAR_CANE, Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK));

    @Override
    public String getVersion() {
        return "1.13_R2";
    }

    @Override
    public void breakBlock(final Player player, final Location loc) {
        ((CraftPlayer)player).getHandle().playerInteractManager.breakBlock(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    @Override
    public Sound getBlockBreakSound(final org.bukkit.block.Block block) {
        try {
            final WorldServer nmsWorld = ((CraftWorld)block.getWorld()).getHandle();
            final Block nmsBlock = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
            final SoundEffectType soundEffectType = nmsBlock.getStepSound();
            final Field breakSound = SoundEffectType.class.getDeclaredField("q");
            breakSound.setAccessible(true);
            final SoundEffect nmsSound = (SoundEffect)breakSound.get((Object)soundEffectType);
            final Field keyField = SoundEffect.class.getDeclaredField("a");
            keyField.setAccessible(true);
            final MinecraftKey nmsString = (MinecraftKey)keyField.get((Object)nmsSound);
            return Sound.valueOf((String)nmsString.getKey().replace(".", "_").toUpperCase());
        }
        catch (final IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Material> getExlcudedBlocks() {
        for (final Material m : Material.values()) {
            if (!m.name().contains("POTTED")) continue;
            excludedMaterials.add(m);
        }
        return excludedMaterials;
    }
}

