package me.binarynetwork.core.gui;

import me.binarynetwork.core.common.utils.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Bench on 9/27/2016.
 */
public class GUIItemExample implements GUIItem {

    private Material material;

    public GUIItemExample(Material material)
    {
        this.material = material;
    }

    @Override
    public ItemStack getItem(Player player)
    {
        return new ItemStack(material);
    }

    @Override
    public void click(Player player, ClickType clickType)
    {
        PlayerUtil.message(player, material.toString());
    }
}
