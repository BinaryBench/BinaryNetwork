package me.binarynetwork.hub.world;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoBlockBreak;
import me.binarynetwork.core.component.components.NoBlockPlace;
import me.binarynetwork.core.component.components.NoDamage;
import me.binarynetwork.core.component.components.WeatherComponent;
import me.binarynetwork.core.component.world.WorldManager;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.hub.world.respawn.RespawnManager;
import me.binarynetwork.hub.world.respawn.respawns.DiagonalRespawn;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/17/2016.
 */
public class HubWorldManager implements Listener, Supplier<World>, Predicate<World> {

    public static final String DEFAULT_WORLD_NAME = "world";
    public static final String HUB_WORLD_NAME = "HubWorld";

    private SimpleComponentWrapper componentWrapper;
    private RespawnManager respawnManager;

    private World world;

    public HubWorldManager(PlayerHolder playerHolder)
    {
        if (Bukkit.getWorlds().size() > 0)
        {
            ServerUtil.shutdown("The main world is already loaded! Set 'load: STARTUP' in the plugin.yml");
            return;
        }
        File worldFolder = new File(Bukkit.getWorldContainer(), DEFAULT_WORLD_NAME);
        try
        {
            FileUtils.deleteDirectory(worldFolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ServerUtil.shutdown("Failed to delete " + HUB_WORLD_NAME + "!");
            return;
        }

        File saveFile = FileUtil.newFileIgnoreCase(ServerUtil.getPlugin().getDataFolder(), HUB_WORLD_NAME + ".zip");

        Log.debugf("Exists: %s", saveFile.exists());

        if (!FileUtil.unZip(saveFile, worldFolder))
        {
            ServerUtil.shutdown("Unable to unzip " + saveFile.getName() + "!");
            return;
        }
        Log.debugf("Unzipped!");

        ServerUtil.registerEvents(this);
        this.componentWrapper = new SimpleComponentWrapper();
        this.respawnManager = new RespawnManager(playerHolder, this, pair -> spawnPlayer(pair.getRight(), pair.getLeft()));
        this.respawnManager.addModule(new DiagonalRespawn());

        getWrapper().addComponent(
                respawnManager,
                new NoDamage(playerHolder),
                new NoBlockPlace(playerHolder),
                new NoBlockBreak(playerHolder),
                new WeatherComponent(this));

    }

    @EventHandler
    public void worldLoad(WorldLoadEvent event)
    {
        World world = event.getWorld();
        if (!world.getName().equals(DEFAULT_WORLD_NAME))
            return;
        this.world = world;
        getWrapper().enable();
    }

    public void spawnPlayer(Location location, Player player)
    {
        player.setGameMode(GameMode.ADVENTURE);
        PlayerUtil.resetPlayer(player);
        PlayerUtil.teleport(player, location);

    }





    @Override
    public boolean test(World world)
    {
        return world.getName().equalsIgnoreCase(DEFAULT_WORLD_NAME);
    }

    @Override
    public World get()
    {
        return world;
    }

    public SimpleComponentWrapper getWrapper()
    {
        return componentWrapper;
    }
}
