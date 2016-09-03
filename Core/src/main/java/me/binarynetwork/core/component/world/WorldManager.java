package me.binarynetwork.core.component.world;

import org.bukkit.World;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by Bench on 9/3/2016.
 */
public interface WorldManager extends WorldConfiguration, Supplier<World>, Predicate<World> {

    @Override
    default boolean test(World world)
    {
        return get() != null & get().equals(world);
    }

}
