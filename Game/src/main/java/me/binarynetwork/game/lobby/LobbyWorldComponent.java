package me.binarynetwork.game.lobby;

import me.binarynetwork.core.BinaryNetworkPlugin;
import me.binarynetwork.core.common.utils.*;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoBlockBreak;
import me.binarynetwork.core.component.components.NoBlockPlace;
import me.binarynetwork.core.component.components.WeatherComponent;
import me.binarynetwork.game.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/3/2016.
 */
public class LobbyWorldComponent extends SimpleComponentWrapper implements Supplier<World>, Predicate<World> {

    public static final String LOBBY_WORLD_NAME = "LobbyWorld";

    private ScheduledExecutorService scheduler;

    private File saveFile;
    private String worldName;
    private World world;
    private Runnable somethingWentWrong;

    public LobbyWorldComponent(ScheduledExecutorService scheduler)
    {
        this(scheduler, () -> ServerUtil.shutdown("#BukkitsFault"));
    }

    public LobbyWorldComponent(ScheduledExecutorService scheduler, Runnable somethingWentWrong)
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

        saveFile = FileUtil.newFileIgnoreCase(dataFolder, LOBBY_WORLD_NAME);

        if (!WorldUtil.isWorld(saveFile))
        {
            System.err.println("Unable to find Lobby World at: " + saveFile.getPath());
            somethingWentWrong.run();
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
        WorldUtil.deleteWorld(getWorldName(), getScheduler(), BinaryNetworkPlugin.getPlugin(), () -> {
            this.world = WorldUtil.createTemporaryWorld(getSaveFile(), getWorldName());
            if (world != null)
            {
                this.world.setAutoSave(false);
                Arrays.asList("doFireTick", "doMobSpawning", "mobGriefing", "naturalRegeneration").forEach(string -> world.setGameRuleValue(string, "false"));
            }

        });
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                WorldUtil.deleteWorld(getWorld(), getScheduler(), BinaryNetworkPlugin.getPlugin()), 60);
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
