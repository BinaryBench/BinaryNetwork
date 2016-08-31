package me.binarynetwork.core.packetInterceptor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketHandler extends ChannelDuplexHandler {
    private final Player player;

    private PacketHandler(Player player) {
        this.player = player;
    }

    public static void hook(Player player) {
        final Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addBefore("packet_handler", "BinaryNetwork", new PacketHandler(player));
    }

    public static void unHook(Player player) {
        final Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> channel.pipeline().remove("Tropilus"));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        PacketEvent event = new PacketEvent(player, PacketEvent.PacketType.OUTWARDS, (Packet) msg);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled())
            super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketEvent event = new PacketEvent(player, PacketEvent.PacketType.INWARDS, (Packet) msg);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled())
            super.channelRead(ctx, msg);
    }

}
