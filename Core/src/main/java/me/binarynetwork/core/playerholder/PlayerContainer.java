package me.binarynetwork.core.playerholder;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Created by Bench on 9/20/2016.
 *
 * A PlayerContainer is like a PlayerHolder, but will not fire events on join & leave
 */
public interface PlayerContainer extends Predicate<Player>, Iterable<Player> {

    Collection<Player> getPlayers();

    @Override
    default Iterator<Player> iterator()
    {
        return getPlayers().iterator();
    }

    @Override
    default boolean test(Player player)
    {
        return getPlayers().contains(player);
    }

}