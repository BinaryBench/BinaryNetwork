package me.binarynetwork.core.component.world;

import org.bukkit.World;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/3/2016.
 */
public interface WorldManager extends Supplier<World>, Predicate<World> {

    @Override
    default boolean test(World world)
    {
        return world.getName().equals(getWorldName());
    }

    void getWorld(Consumer<World> callback);

    String getWorldName();

}
