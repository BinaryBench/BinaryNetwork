package me.binarynetwork.core.component.world;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.*;
import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.constructor.Construct;


import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/2/2016.
 */
public class GameWorldComponent extends ConfiguredWorldComponent {

    public GameWorldComponent(ScheduledExecutorService scheduler, String gameName)
    {
        this(scheduler, gameName, () -> ServerUtil.shutdown("#BlameBukkit"));
    }

    public GameWorldComponent(@Nonnull ScheduledExecutorService scheduler, @Nonnull String gameName, Runnable onFail)
    {
        this(new ConstructorStorage(scheduler, gameName, onFail));
    }

    private GameWorldComponent(ConstructorStorage constructor)
    {
        super(constructor.getScheduler(), constructor.getSaveFile(), constructor.getWorldName(), constructor.getOnFail());
    }

    private static class ConstructorStorage
    {
        private ScheduledExecutorService scheduler;
        private File saveFile;
        private String worldName;
        private Runnable onFail;

        public ConstructorStorage(ScheduledExecutorService scheduler, String gameName, Runnable onFail)
        {
            this.scheduler = scheduler;
            this.onFail = onFail;

            File dataFolder = ServerUtil.getPlugin().getDataFolder();
            if (!dataFolder.isDirectory())
            {
                System.err.println("Could not find plugin data folder!");
                onFail.run();
                return;
            }

            File worldsFolder = FileUtil.newFileIgnoreCase(dataFolder, "Worlds");
            if (!worldsFolder.isDirectory())
            {
                System.err.println("Worlds folder not found! Creating one...");
                worldsFolder.mkdirs();
                onFail.run();
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
                onFail.run();
                return;
            }

            saveFile = RandomUtil.randomElement(possibleWorlds);

            //Using the hashcode is a hack and should probably be changed at some point
            this.worldName = saveFile.getName() + "-" + gameName + "-" + hashCode();
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

        public Runnable getOnFail()
        {
            return onFail;
        }
    }
}
