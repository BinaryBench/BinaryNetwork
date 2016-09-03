package me.binarynetwork.core.component.world;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.*;
import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 9/2/2016.
 */
public class SimpleWorldComponent extends ListenerComponent implements WorldManager {

    private ScheduledExecutorService scheduler;

    private File saveFile;
    private String worldName;
    private World world;
    private Runnable somethingWentWrong;
    private Map<String, YamlConfiguration> configurations = new HashMap<>();

    public SimpleWorldComponent(ScheduledExecutorService scheduler, String gameName)
    {
        this(scheduler, gameName, () -> ServerUtil.shutdown("#BlameBukkit"));
    }

    public SimpleWorldComponent(ScheduledExecutorService scheduler, String gameName, Runnable somethingWentWrong)
    {
        this.scheduler = scheduler;
        this.somethingWentWrong = somethingWentWrong;
        File dataFolder = BinaryNetworkPlugin.getPlugin().getDataFolder();

        if (!dataFolder.isDirectory())
        {
            System.err.println("Could not find plugin data folder!");
            somethingWentWrong.run();
            return;
        }

        File worldsFolder = FileUtil.newFileIgnoreCase(dataFolder, "Worlds");

        if (!worldsFolder.isDirectory())
        {
            System.err.println("Worlds folder not found! Creating one...");
            worldsFolder.mkdirs();
            somethingWentWrong.run();
            return;
        }

        List<File> possibleWorlds = new ArrayList<>();

        File[] files = worldsFolder.listFiles();

        if (files == null)
            files = new File[]{};

        for (File file : files)
        {
            if (WorldUtil.isWorld(file))
            {
                File configurationFile = FileUtil.newFileIgnoreCase(file, "mapdata.yml");

                if (!configurationFile.exists())
                {
                    System.err.println("World " + file.getName() + " does not have a mapdata.yml file!");
                    continue;
                }

                YamlConfiguration mapdata = YamlConfiguration.loadConfiguration(configurationFile);

                if (ListUtil.containsIgnoreCase(mapdata.getStringList("Games"), gameName))
                    possibleWorlds.add(file);

            }
        }

        if (possibleWorlds.isEmpty())
        {
            System.err.println("No worlds found for game: " + gameName);
            somethingWentWrong.run();
            return;
        }

        saveFile = RandomUtil.randomElement(possibleWorlds);

        //Using the hashcode is a hack and should probably be changed at some point
        this.worldName = saveFile.getName() + "-" + gameName + "-" + hashCode();
    }

    @Override
    public void onEnable()
    {
        WorldUtil.deleteWorld(getWorldName(), getScheduler(), BinaryNetworkPlugin.getPlugin(), () -> {
            this.world = WorldUtil.createTemporaryWorld(getSaveFile(), getWorldName());
            if (world != null)
                this.world.setAutoSave(false);
        });
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().runTaskLater(BinaryNetworkPlugin.getPlugin(), () ->
                WorldUtil.deleteWorld(getWorld(), getScheduler(), BinaryNetworkPlugin.getPlugin()), 60);
    }

    public YamlConfiguration getConfig(String configName)
    {
        if (configurations.containsKey(configName))
            return configurations.get(configName);

        String fileName = configName + ".yml";

        File configurationFile = FileUtil.newFileIgnoreCase(getSaveFile(), fileName);

        if (!configurationFile.exists())
        {
            System.err.println("World " + getSaveFile().getName() + " does not have a " + fileName + " file!");
            getSomethingWentWrong().run();
            return null;
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configurationFile);
        configurations.put(configName, configuration);
        return configuration;
    }

    @Override
    public String getSaveName()
    {
        return saveFile.getName();
    }

    public Runnable getSomethingWentWrong()
    {
        return somethingWentWrong;
    }

    @Override
    public World get()
    {
        return world;
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public File getSaveFile()
    {
        return saveFile;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public World getWorld()
    {
        return world;
    }
}
