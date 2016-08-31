package me.binarynetwork.core.common.utils;


import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Created by Bench on 8/29/2016.
 */
public class BlockUtil {
    private BlockUtil() {}

    @SuppressWarnings( "deprecation" )
    public static ItemStack toItemStack(Block block)
    {
        return new ItemStack(block.getType(), 1, block.getData());
    }

    @SuppressWarnings( "deprecation" )
    public static MaterialData toMaterialData(Block block)
    {
        return new MaterialData(block.getType(), block.getData());
    }
}
