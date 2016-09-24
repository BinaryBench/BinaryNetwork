package me.binarynetwork.hub.respawn.respawns;

import me.binarynetwork.hub.respawn.RespawnModule;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/17/2016.
 */
public class DiagonalRespawn implements RespawnModule {

    @Override
    public Pair<Location, Boolean> check(Player player)
    {
        Location playersLocation = player.getLocation();

        if (playersLocation.getY() > 60)
            return null;

        World world = playersLocation.getWorld();
        double x = playersLocation.getX() - 0.5;
        double z = playersLocation.getZ() - 0.5;

        Location spawn;

        if (z < x)
        {
            if (z > -x)
            {
                spawn = new Location(world, 10.5, 80, 0.5, -90, 0);
                //Log.debug("east");
            }
            else {
                spawn = new Location(world, 0.5, 80, -9.5, 180, 0);
                //Log.debug("north");
            }
        }
        else {
            if (z > -x)
            {
                spawn = new Location(world, 0.5, 80, 10.5, 0, 0);
                //Log.debug("south");
            }
            else {
                spawn = new Location(world, -9.5, 80, 0.5, 90, 0);
                //Log.debug("west");
            }
        }

        if (Math.abs(x) < 7 && Math.abs(z) < 7)
        {
            spawn = new Location(world, 0.5, 80, 0.5, 0, 0);
        }
        else if (Math.abs(x) < 17 && Math.abs(z) < 17)
        {
            spawn.setYaw(Math.round(playersLocation.getYaw() / 90) * 90);
        }

        return Pair.of(spawn, true);
    }
}
