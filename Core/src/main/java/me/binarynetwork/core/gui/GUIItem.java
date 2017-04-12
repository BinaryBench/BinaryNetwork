package me.binarynetwork.core.gui;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Bench on 9/27/2016.
 */
public interface GUIItem {

    ItemStack getItem(Player player);

    default void open(Player player){}

    void click(Player player, ClickType clickType);

    default void close(Player player){}

}
