package me.binarynetwork.core.component.world;

import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.ListenerComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.core.jmx.Server;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Created by Bench on 9/20/2016.
 */
public class PlayerDataPurgeComponent extends ListenerComponent {

    private Predicate<World> worldPredicate;

    private ScheduledExecutorService scheduler;

    public PlayerDataPurgeComponent(Predicate<World> worldPredicate, ScheduledExecutorService scheduler)
    {
        this.worldPredicate = worldPredicate;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        scheduler.schedule(this::clearPlayerDatas, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onEnable()
    {
        scheduler.execute(this::clearPlayerDatas);
    }

    @Override
    public void onDisable()
    {
        scheduler.execute(this::clearPlayerDatas);
    }

    private void clearPlayerDatas()
    {
        Bukkit.getWorlds().stream().filter(world -> worldPredicate.test(world)).forEach(world -> clearPlayerData(world.getName()));
    }

    private void clearPlayerData(String worldName)
    {
        File worldsFolder = WorldUtil.getWorldFile(worldName);
        if (!worldsFolder.exists())
            return;
        File[] purgeDirs = new File[]{FileUtil.newFileIgnoreCase(worldsFolder, "playerdata"), FileUtil.newFileIgnoreCase(worldsFolder, "stats")};

        for (File purgeDir : purgeDirs)
        {
            if (!purgeDir.isDirectory())
                continue;
            File[] files = purgeDir.listFiles();
            if (files == null)
                continue;
            for (File file : files)
            {
                String uuidString = FilenameUtils.getBaseName(file.getName());
                boolean isOnline;
                try
                {
                    isOnline = ServerUtil.isOnline(UUID.fromString(uuidString));
                }
                catch (IllegalArgumentException e)
                {
                    isOnline = false;
                }
                if (!isOnline && !file.delete())
                        try
                        {
                            FileUtils.forceDelete(file);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }

            }
        }

    }

}
