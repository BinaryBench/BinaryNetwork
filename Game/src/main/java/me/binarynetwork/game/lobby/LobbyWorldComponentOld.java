package me.binarynetwork.game.lobby;

import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoBlockBreak;
import me.binarynetwork.core.component.components.NoBlockPlace;
import me.binarynetwork.core.component.components.WeatherComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/3/2016.
 */
public class LobbyWorldComponentOld extends SimpleComponentWrapper implements Supplier<World>, Predicate<World> {

    public static final String LOBBY_WORLD_NAME = "LobbyWorld";

    private ScheduledExecutorService scheduler;

    private File saveFile;
    private String worldName;
    private World world;
    private Runnable onFail;

    public LobbyWorldComponentOld(ScheduledExecutorService scheduler)
    {
        this(scheduler, () -> ServerUtil.shutdown("#BukkitsFault"));
    }

    public LobbyWorldComponentOld(ScheduledExecutorService scheduler, Runnable onFail)
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

        saveFile = FileUtil.newFileIgnoreCase(dataFolder, LOBBY_WORLD_NAME);

        if (!WorldUtil.isWorld(saveFile))
        {
            System.err.println("Unable to find Lobby World at: " + saveFile.getPath());
            onFail.run();
            return;
        }

        //Using the hashcode is a hack and should probably be changed at some point
        this.worldName = saveFile.getName() + "-" + hashCode();

        Predicate<Player> inLobbyPredicate = player -> getWorld() != null && player.getWorld().equals(getWorld());

        addComponent(
                new NoBlockBreak(inLobbyPredicate),
                new NoBlockPlace(inLobbyPredicate),
                new WeatherComponent(this)
        );

    }

    @Override
    public boolean test(World world)
    {
        return getWorld() != null && world.equals(getWorld());
    }

    @Override
    public void onEnable()
    {
        WorldUtil.deleteWorld(getWorldName(), getScheduler(), (aBoolean) -> {

            this.world = WorldUtil.createTemporaryWorld(getSaveFile(), getWorldName());
            if (world != null)
            {
                this.world.setAutoSave(false);
                Arrays.asList("doFireTick", "doMobSpawning", "mobGriefing", "naturalRegeneration").forEach(string -> world.setGameRuleValue(string, "false"));
                world.setGameRuleValue("randomTickSpeed", "0");
            }
        });
    }

    @Override
    public void onDisable()
    {
        Scheduler.runSyncLater(() -> WorldUtil.deleteWorld(getWorld(), getScheduler()), 60);
    }

    public Runnable getOnFail()
    {
        return onFail;
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
