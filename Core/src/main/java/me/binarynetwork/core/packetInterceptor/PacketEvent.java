package me.binarynetwork.core.packetInterceptor;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private PacketType packetType;
    private boolean cancelled = false;
    private Packet packet;
    private Player player;

    public PacketEvent(Player player, PacketType packetType, Packet packet) {
        this.packetType = packetType;
        this.packet = packet;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Packet getPacket() {
        return packet;
    }

    public Player getPlayer() {
        return player;
    }

    public static enum PacketType {
        INWARDS, OUTWARDS
    }
}
