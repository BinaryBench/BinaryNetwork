package me.binarynetwork.core.gui;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import me.binarynetwork.core.common.utils.MapUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bench on 9/27/2016.
 */
public class GUIInventory implements GUI, Listener {

    private Map<Player, Inventory> inventoryMap;
    private Map<Integer, GUIItem> itemMap;

    private int size;
    private String title;

    public GUIInventory(int size, String title)
    {
        this(size, title, null);
    }

    public GUIInventory(int size, String title, @Nullable HashMap<Integer, GUIItem> items)
    {
        this.inventoryMap = new HashMap<>();
        this.itemMap = new HashMap<>();

        this.size = size;
        this.title = title;

        if (items != null)
            this.itemMap.putAll(items);

        ServerUtil.registerEvents(this);
    }

    @Override
    public void open(Player player)
    {
        Inventory inventory = Bukkit.createInventory(player, size, title);

        Map<GUIItem, ItemStack> items = new HashMap<>();

        for (Map.Entry<Integer, GUIItem> entry : this.itemMap.entrySet())
        {
            ItemStack item = items.get(entry.getValue());
            if (item == null)
            {
                item = entry.getValue().getItem(player);
                items.put(entry.getValue(), item);
            }
            inventory.setItem(entry.getKey(), item);
        }
        this.inventoryMap.put(player, inventory);
        player.openInventory(inventory);
    }

    public boolean update(GUIItem guiItem)
    {
        boolean modified = false;
        if (!itemMap.containsValue(guiItem))
            return false;
        for (Player player : inventoryMap.keySet())
            if (update(player, guiItem))
                modified = true;
        return modified;
    }

    public boolean update(Integer slot)
    {
        boolean modified = false;
        if (!itemMap.containsKey(slot))
            return false;
        for (Player player : inventoryMap.keySet())
            if (update(player, slot))
                modified = true;
        return modified;
    }

    public boolean update(Player player, GUIItem guiItem)
    {
        Inventory inventory = inventoryMap.get(player);
        if (inventory == null)
            return false;
        ItemStack itemStack = guiItem.getItem(player);
        for (Integer integer : MapUtil.getKeys(this.itemMap, guiItem))
            inventory.setItem(integer, itemStack);
        return true;
    }

    public boolean update(Player player, Integer integer)
    {
        Inventory inventory = inventoryMap.get(player);

        if (inventory == null)
            return false;

        GUIItem item = this.itemMap.get(integer);

        inventory.setItem(integer, item == null ? null : item.getItem(player));

        return true;
    }

    public boolean addItem(int slot, GUIItem item)
    {
        return addItem(item, slot);
    }

    public boolean addItem(GUIItem item, int... slots)
    {
        boolean modified = false;
        for (int i : slots)
            if (itemMap.put(i, item) != null)
                modified = true;
        return modified;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();
        InventoryView inventory = event.getView();

        if (checkRemove(player, inventory.getTopInventory()))
            return;

        event.setCancelled(true);

        GUIItem guiItem = itemMap.get(event.getRawSlot());

        if (guiItem == null)
            return;

        guiItem.click(player, event.getClick());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player))
            return;

        checkRemove((Player) event.getPlayer(), null);
    }

    private boolean checkRemove(Player player, Inventory inventory)
    {
        Inventory mappedInventory;

        if ((mappedInventory = inventoryMap.get(player)) == null)
            return true;

        if (mappedInventory.equals(inventory))
            return false;

        inventoryMap.remove(player);
        return true;
    }

}
