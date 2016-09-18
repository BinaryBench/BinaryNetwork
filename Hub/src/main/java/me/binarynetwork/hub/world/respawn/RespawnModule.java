package me.binarynetwork.hub.world.respawn;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/17/2016.
 */
public interface RespawnModule {

    Pair<Location, Boolean> check(Player player);

}
