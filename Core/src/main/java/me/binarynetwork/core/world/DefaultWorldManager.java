package me.binarynetwork.core.world;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.Properties;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/20/2016.
 */
public class DefaultWorldManager implements Listener, Supplier<World>, Predicate<World> {

    public String defaultWorldName = "world";
    public String HUB_WORLD_NAME = "HubWorld";

    private World world;

    public DefaultWorldManager(File saveFile)
    {
        defaultWorldName = Properties.getServerPropertyString(Properties.ServerProperty.LEVEL_NAME, defaultWorldName);
        if (Bukkit.getWorlds().size() > 0)
        {
            ServerUtil.shutdown("The main world is already loaded!  Execute this onLoad() or set 'load: STARTUP' in the plugin.yml.");
            return;
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), defaultWorldName);

        try
        {
            FileUtils.deleteDirectory(worldFolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ServerUtil.shutdown("Failed to delete " + defaultWorldName + "!");
            return;
        }


        Log.debugf("Exists: %s", saveFile.exists());

        if (!FileUtil.unZip(saveFile, worldFolder))
        {
            try
            {
                FileUtils.copyDirectory(saveFile, worldFolder);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                ServerUtil.shutdown("Unable to copy " + saveFile.getName() + "!");
                return;
            }
        }
        Log.debugf("Copied!");
        ServerUtil.registerEvents(new Listener() {
            @EventHandler
            public void onLoad(WorldLoadEvent event)
            {
                World eventWorld = event.getWorld();
                if (!test(eventWorld))
                    return;
                world = eventWorld;
                worldLoad(world);
            }
        });
    }

    public void worldLoad(World world)
    {

    }

    @Override
    public boolean test(World world)
    {
        return world.getName().equalsIgnoreCase(defaultWorldName);
    }

    @Override
    public World get()
    {
        return world;
    }
}
