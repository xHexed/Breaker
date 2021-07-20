package com.github.xhexed.breaker.manager;

import com.github.xhexed.breaker.core.BreakingCore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketManager {
    public static void addPlayer(final Player player) {
        final Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            if (channel.pipeline().get("PacketInjector") == null) {
                channel.pipeline().addBefore("packet_handler", "PacketInjector", new ChannelDuplexHandler() {
                    @Override
                    public void channelRead(final ChannelHandlerContext channel, final Object object) throws Exception {
                        BreakingCore.handlePacket(object, player);
                        super.channelRead(channel, object);
                    }
                });
            }
        });
    }
}
