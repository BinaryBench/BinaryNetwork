package me.binarynetwork.core.gui;

import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/27/2016.
 */
public class GUIInventoryExample extends GUIInventory {
    public GUIInventoryExample(ScheduledExecutorService scheduler)
    {
        super(18, "Example");
        addItem(new GUIItemRotate(this, scheduler), 4);
    }
}
