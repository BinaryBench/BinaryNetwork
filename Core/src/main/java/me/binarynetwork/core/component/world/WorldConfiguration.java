package me.binarynetwork.core.component.world;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/3/2016.
 */
public interface WorldConfiguration extends WorldManager {
    void getConfig(Consumer<YamlConfiguration> callback);
    default void getWorldAndConfig(BiConsumer<World, YamlConfiguration> callback)
    {
        getWorld(world ->
            getConfig(yamlConfiguration ->
                    callback.accept(world, yamlConfiguration))
        );
    }
}
