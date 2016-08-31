package me.binarynetwork.core.playerholder;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Created by Bench on 8/28/2016.
 */
public interface PlayerHolder extends Predicate<Player>, Iterable<Player> {

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
