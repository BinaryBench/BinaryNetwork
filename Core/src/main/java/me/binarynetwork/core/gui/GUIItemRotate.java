package me.binarynetwork.core.gui;

import com.mysql.jdbc.TimeUtil;
import me.binarynetwork.core.common.scheduler.SyncRunnable;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bench on 10/1/2016.
 */
public class GUIItemRotate implements GUIItem, SyncRunnable {

    private ScheduledExecutorService scheduler;

    private GUIInventory inventory;
    private Material[] materials;
    private int counter;
    private ItemStack itemStack;

    public GUIItemRotate(GUIInventory inventory, ScheduledExecutorService scheduler)
    {
        this.inventory = inventory;
        materials = new Material[]{Material.APPLE,
                Material.CARROT_ITEM,
                Material.ROTTEN_FLESH,
                Material.COOKED_BEEF,
                Material.COOKED_CHICKEN,
                Material.COOKED_BEEF};
        this.scheduler = scheduler;
        getScheduler().scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public ItemStack getItem(Player player)
    {
        return itemStack;
    }

    @Override
    public void click(Player player, ClickType clickType)
    {
        PlayerUtil.message(player, String.format("Mat: %s\nClickType: %s", materials[counter], clickType));
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    @Override
    public void syncRun()
    {
        counter++;
        if (counter == materials.length)
            counter = 0;
        this.itemStack = new ItemStack(materials[counter]);
        getInventory().update(this);
    }

    public GUIInventory getInventory()
    {
        return inventory;
    }
}
