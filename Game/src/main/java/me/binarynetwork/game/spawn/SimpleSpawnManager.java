package me.binarynetwork.game.spawn;

import me.binarynetwork.core.common.utils.LocationUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.component.world.WorldConfiguration;
import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bench on 9/3/2016.
 */
public class SimpleSpawnManager extends ListenerComponent implements SpawnManager {

    private PlayerHolder playerHolder;
    private List<Location> locations;

    public SimpleSpawnManager(WorldConfiguration worldManager, PlayerHolder playerHolder)
    {
        this.playerHolder = playerHolder;

        worldManager.getWorldAndConfig((world, mapdata) ->
        {
            locations = new ArrayList<>();

            for (String stringLocation : mapdata.getStringList("SpawnPoints"))
            {
                Location l = LocationUtil.toLocation(world, stringLocation);
                if (l != null)
                    locations.add(l);
            }

            if (locations.isEmpty()) {
                ServerUtil.shutdown("There where no valid spawnpoints found for the map " + worldManager.getWorldName() + "!",
                        "Scotty did not know where to beam you down to!");
            }
        });

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
