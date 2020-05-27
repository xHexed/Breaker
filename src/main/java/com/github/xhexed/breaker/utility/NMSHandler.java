package com.github.xhexed.breaker.utility;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.core.BreakingCore;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMSHandler {
    private static final Pattern DOT = Pattern.compile(".", Pattern.LITERAL);
    private static final List<Material> excludedMaterials = new ArrayList<>(Arrays.asList(Material.AIR, Material.GRASS, Material.END_ROD, Material.BARRIER, Material.TORCH, Material.getMaterial("REDSTONE_TORCH_ON"), Material.getMaterial("REDSTONE_TORCH_OFF"), Material.getMaterial("LONG_GRASS"), Material.getMaterial("BEETROOT_BLOCK"), Material.WHEAT, Material.POTATO, Material.CARROT, Material.getMaterial("SAPLING"), Material.FLOWER_POT, Material.getMaterial("YELLOW_FLOWER"), Material.getMaterial("RED_ROSE"), Material.getMaterial("DOUBLE_PLANT"), Material.getMaterial("WATER_LILY"), Material.FIRE, Material.DEAD_BUSH, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.NETHER_WART_BLOCK, Material.REDSTONE_WIRE, Material.getMaterial("REDSTONE_COMPARATOR_OFF"), Material.getMaterial("REDSTONE_COMPARATOR_ON"), Material.SLIME_BLOCK, Material.getMaterial("DIODE_BLOCK_OFF"), Material.getMaterial("DIODE_BLOCK_ON"), Material.STRUCTURE_VOID, Material.getMaterial("SUGAR_CANE_BLOCK"), Material.TNT, Material.TRIPWIRE, Material.TRIPWIRE_HOOK));
    private static Field breaksound;
    private static Field minecraftKey;

    static {
        try {
            breaksound = SoundEffectType.class.getDeclaredField("o");
            breaksound.setAccessible(true);

            minecraftKey = SoundEffect.class.getDeclaredField("b");
            minecraftKey.setAccessible(true);
        }
        catch (final NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public NMSHandler() {
        Breaker.debug("Loaded NMS Version: 1.11_R1", 0);
    }

    public static void breakAnimation(final int stage, final Block block, final EntityHuman player) {

        ((CraftServer) Bukkit.getServer()).getHandle().sendPacketNearby(
                player,
                block.getX(),
                block.getY(),
                block.getZ(),
                120,
                ((CraftWorld) block.getWorld()).getHandle().dimension,
                new PacketPlayOutBlockBreakAnimation(BreakingCore.getBlockEntityId(block),
                                                     new BlockPosition(block.getX(), block.getY(), block.getZ()),
                                                     stage
                                                     )
        );
    }

    public static void breakBlock(final Player player, final Location loc) {
        ((CraftPlayer)player).getHandle().playerInteractManager.breakBlock(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public static Sound getBlockBreakSound(final Block block) {
        try {
            final SoundEffectType soundEffectType = CraftMagicNumbers.getBlock(block).getStepSound();
            final SoundEffect soundEffect = (SoundEffect) breaksound.get(soundEffectType);
            final MinecraftKey minecraftKey = (MinecraftKey) NMSHandler.minecraftKey.get(soundEffect);
            return Sound.valueOf(DOT.matcher(minecraftKey.a()).replaceAll(Matcher.quoteReplacement("_")).toUpperCase());
        }
        catch (final IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static List<Material> getExlcudedBlocks() {
        return excludedMaterials;
    }
}
