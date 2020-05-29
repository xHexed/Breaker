package com.github.xhexed.breaker.utility;

import com.github.xhexed.breaker.Breaker;
import com.github.xhexed.breaker.core.BreakingCore;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMSHandler {
    private static final Pattern DOT = Pattern.compile(".", Pattern.LITERAL);
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

    public static void breakAnimation(final int stage, final Block block, final Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutBlockBreakAnimation(BreakingCore.getBlockEntityId(block),
                                                     new BlockPosition(block.getX(), block.getY(), block.getZ()),
                                                     stage
        ));
    }

    public static void breakBlock(final Player player, final Location loc) {
        ((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
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
}

