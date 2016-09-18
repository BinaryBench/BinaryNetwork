package me.binarynetwork.core.common.utils;

import com.comphenix.protocol.PacketType;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.binarynetwork.core.BinaryNetworkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

/**
 * Created by Bench on 8/28/2016.
 */
public class PlayerUtil {

    private PlayerUtil() {}

    public static void message(CommandSender player, String message)
    {
        player.sendMessage(message);
    }

    public static String getName(Player player)
    {
        return player.getName();
    }

    /**
     *
     * Resets the {@code Player} who's {@code UUID} is {@code playersUUID}
     * Health, Hunger, Walk speed, Fall Distance, Fire Ticks, PotionEffects
     * and clears the player's inventory.
     *
     * @param player The player
     */
    public static void resetPlayer(Player player)
    {

        if (!hasPlayer(player))
            return;

        clearInv(player);
        clearPotionEffects(player);
        player.resetMaxHealth();
        resetHealth(player);
        resetMaxHunger(player);
        resetWalkSpeed(player);
        player.setFallDistance(0);
        player.setFireTicks(0);
    }

    /**
     *
     * Sets the {@code Player} who's {@code UUID} is {@code playersUUID}
     * Health to their maxHealth.
     *
     * @param player The player
     */
    public static void resetHealth(Player player)
    {
        if (!hasPlayer(player))
            return;

        player.setHealth(player.getMaxHealth());
    }

    /**
     *
     * Sets the {@code Player} who's {@code UUID} is {@code playersUUID}
     * Hunger and Saturation to 20.
     *
     * @param player The player
     */
    public static void resetMaxHunger(Player player)
    {
        if (!hasPlayer(player))
            return;


        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    /**
     *
     * Clears the {@code Player} who's {@code UUID} is {@code playersUUID}
     * Inventory.
     *
     * @param player The player
     */
    public static void clearInv(Player player)
    {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
    }

    /**
     *
     * Removes all potion effects from the {@code Player} who's {@code UUID}
     * is {@code playersUUID}.
     *
     * @param player The player
     */
    public static void clearPotionEffects(Player player)
    {
        if (!hasPlayer(player))
            return;

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    /**
     *
     * Sets the {@code Player} who's {@code UUID} is {@code playersUUID}
     * WalkSpeed to {@code 0.2f}.
     *
     * @param player The player
     */
    public static void resetWalkSpeed(Player player)
    {
        if (!hasPlayer(player))
            return;

        player.setWalkSpeed(0.2f);
    }

    /**
     *
     * Resets the {@code Player}'s fall distance to 0.
     *
     * @param player The player
     */
    public static void resetFallDistance(Player player)
    {
        if (!hasPlayer(player))
            return;

        player.setFallDistance(0);
    }

    /**
     *
     * Checks to see if there is a {@code Player} online who has the UUID
     * {@code playersUUID}
     *
     * @param player The player
     */
    public static boolean hasPlayer(Player player)
    {
        return player.isOnline();
    }

    public static void killPlayer(Player player)
    {
        if (!hasPlayer(player))
            return;

        player.damage(player.getHealth());
    }

    public static Player getPlayer(Entity entity)
    {
        if (entity instanceof Player)
            return (Player) entity;
        else
            return null;
    }

    public static void sendToServer(Player player, String server)
    {
        Plugin plugin = BinaryNetworkPlugin.getPlugin();
        if (!Bukkit.getServer().getMessenger().isOutgoingChannelRegistered(plugin, "BungeeCord"))
            Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public static void teleport(Player player, Location location)
    {
        player.teleport(location);
    }


}
