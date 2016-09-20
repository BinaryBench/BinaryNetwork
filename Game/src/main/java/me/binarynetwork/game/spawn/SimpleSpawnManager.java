package me.binarynetwork.game.spawn;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.component.world.WorldManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bench on 9/3/2016.
 */
public class SimpleSpawnManager extends ListenerComponent implements SpawnManager {
    private WorldManager worldManager;
    private PlayerHolder playerHolder;
    private List<Location> locations;

    public SimpleSpawnManager(WorldManager worldManager, PlayerHolder playerHolder)
    {
        this.worldManager = worldManager;
        this.playerHolder = playerHolder;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
        Log.debugf("event called!");
        if (!worldManager.test(event.getWorld()))
            return;

        loadLocations(event.getWorld());
    }

    @Override
    public void onEnable()
    {
        World world = worldManager.get();
        if (world != null)
            loadLocations(world);
    }

    public void loadLocations(World world)
    {

        locations = new ArrayList<>();

        YamlConfiguration mapdata = worldManager.getConfig("mapdata");

        for (String stringLocation : mapdata.getStringList("SpawnPoints"))
        {
            Location l = LocationUtil.toLocation(world, stringLocation);

            if (l != null)
                locations.add(l);
        }

        if (locations.isEmpty()) {
            ServerUtil.shutdown("There where no valid spawnpoints found for the map " + worldManager.getSaveName() + "!",
                    "Scotty did not know where to beam you down to!");
        }
    }


    @Override
    public Location getSpawn(Player player)
    {
        if (locations == null || locations.isEmpty())
            ServerUtil.shutdown("No locations, (probably because the world wasn't loaded.)");

        Location bestLoc = null;
        double bestDistance = -1;
        for (Location loc : locations)
        {
            double shortestPlayerDistance = Double.MAX_VALUE;
            for (Player otherPlayer : playerHolder)
            {
                double distance =  (loc.getWorld().equals(otherPlayer.getWorld())) ? loc.distance(otherPlayer.getLocation()) : Double.MAX_VALUE;
                if (distance < shortestPlayerDistance)
                    shortestPlayerDistance = distance;
            }
            if (shortestPlayerDistance > bestDistance)
                bestLoc = loc;


        }

        return bestLoc;
    }
}
