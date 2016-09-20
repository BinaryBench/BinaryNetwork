package me.binarynetwork.game.spawn;

import com.comphenix.protocol.PacketType;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.game.spawn.events.PlayerSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/3/2016.
 */
public interface SpawnManager {

    Location getSpawn(Player player);

    default void respawn(Iterable<Player> players) {
        players.forEach(this::respawn);
    }

    default Location respawn(Player player)
    {
        Location loc = getSpawn(player);
        return respawn(new PlayerSpawnEvent(this, player, player.getLocation(), loc));
    }

    default Location respawn(PlayerSpawnEvent event)
    {
        Player player = event.getPlayer();
        player.teleport(event.getTo());
        Bukkit.getPluginManager().callEvent(event);
        return event.getTo();
    }
}
