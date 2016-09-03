package me.binarynetwork.core.component.components;

import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.component.ListenerComponent;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.util.function.Predicate;

/**
 * Created by Bench on 9/3/2016.
 */
public class WeatherComponent extends ListenerComponent {


    private Predicate<World> worldPredicate;
    private boolean hasStorm;

    public WeatherComponent(Predicate<World> worldPredicate)
    {
        this(worldPredicate, false);
    }

    public WeatherComponent(Predicate<World> worldPredicate, boolean hasStorm)
    {
        this.worldPredicate = worldPredicate;
        this.hasStorm = hasStorm;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event)
    {
        if (!getWorldPredicate().test(event.getWorld()))
            return;
        setWeather(event.getWorld());
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event)
    {
        if (!getWorldPredicate().test(event.getWorld()))
            return;

        if (event.toWeatherState() != hasStorm)
            event.setCancelled(true);
    }

    private void setWeather(World world)
    {
        if (world == null)
            return;

        File file = FileUtil.newFileIgnoreCase(world.getWorldFolder(), "mapdata.yml");

        if (file.exists())
        {
            YamlConfiguration mapdata = YamlConfiguration.loadConfiguration(file);
            hasStorm = mapdata.getBoolean("Storm", hasStorm);
        }

        world.setStorm(hasStorm);
        world.setThundering(hasStorm);
        world.setWeatherDuration(Integer.MAX_VALUE);
    }

    public Predicate<World> getWorldPredicate()
    {
        return worldPredicate;
    }

    public boolean isHasStorm()
    {
        return hasStorm;
    }
}
