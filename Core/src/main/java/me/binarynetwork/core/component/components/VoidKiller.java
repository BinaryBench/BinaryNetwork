package me.binarynetwork.core.component.components;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.core.component.world.WorldManager;
import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created by Bench on 9/3/2016.
 */
public class VoidKiller extends BaseComponent implements Runnable {

    private static final int DEFAULT_HEIGHT = 0;

    private int id;

    private int height = DEFAULT_HEIGHT;

    private PlayerHolder playerHolder;
    private WorldManager worldManager;

    public VoidKiller(PlayerHolder playerHolder, WorldManager worldManager)
    {
        this.playerHolder = playerHolder;
        this.worldManager = worldManager;
        loadHeight();
    }

    private void loadHeight()
    {
        YamlConfiguration mapdata = worldManager.getConfig("mapdata");

        this.height = mapdata.getInt("VoidLevel", DEFAULT_HEIGHT);
    }


    @Override
    public void onEnable()
    {
        this.id = Bukkit.getScheduler().runTaskTimer(BinaryNetworkPlugin.getPlugin(), this, 2, 2).getTaskId();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTask(this.id);
    }





    @Override
    public void run()
    {
        for (Player player : getPlayerHolder())
        {
            Location loc = player.getLocation();
            if (loc.getBlockY() < this.height)
            {
                PlayerUtil.killPlayer(player);
            }
        }
    }


    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public WorldManager getWorldManager()
    {
        return worldManager;
    }
}