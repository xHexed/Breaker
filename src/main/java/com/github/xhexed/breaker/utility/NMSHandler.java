package com.github.xhexed.breaker.utility;

import com.github.xhexed.breaker.core.BreakingCore;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMSHandler {
    private static final Map<Material, Sound> soundList = new EnumMap<>(Material.class);

    public static void cacheSound() {
        final Pattern DOT = Pattern.compile(".", Pattern.LITERAL);
        final Field breaksound;
        final Field minecraftKey;

        try {
            breaksound = SoundEffectType.class.getDeclaredField("o");
            breaksound.setAccessible(true);

            minecraftKey = SoundEffect.class.getDeclaredField("b");
            minecraftKey.setAccessible(true);
        }
        catch (final NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        for (final Material material : Material.values()) {
            if (!material.isBlock()) continue;
            final SoundEffectType soundEffectType = CraftMagicNumbers.getBlock(material).getStepSound();
            try {
                soundList.put(material, Sound.valueOf(DOT.matcher(((MinecraftKey) minecraftKey.get(breaksound.get(soundEffectType))).a()).replaceAll(Matcher.quoteReplacement("_")).toUpperCase()));
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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
        return soundList.get(block.getType());
    }
}

