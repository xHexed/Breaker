package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.core.BreakingCore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketManager {
    public static void addPlayer(final Player player) {
        final Channel channel = getChannel(player);
        if (channel.pipeline().get("PacketInjector") == null) {
            channel.pipeline().addBefore("packet_handler", "PacketInjector", new ChannelDuplexHandler() {
                @Override
                public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
                    super.write(ctx, msg, promise);
                }

                @Override
                public void channelRead(final ChannelHandlerContext channel, final Object object) {
                    BreakingCore.handlePacket(object, player);
                }
            });
        }
    }

    private static Channel getChannel(final Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
    }
}
