package me.binarynetwork.core.component.world;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.FileUtil;
import me.binarynetwork.core.common.utils.WorldUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 10/3/2016.
 */
public class ConfiguredWorldComponent extends WorldComponent implements WorldConfiguration {

    public static final String DEFAULT_CONFIGURATION_NAME = "mapdata";

    private CompletableFuture<YamlConfiguration> configuration;

    public ConfiguredWorldComponent(@Nonnull ScheduledExecutorService scheduler, @Nonnull File saveFile, @Nonnull String worldName, @Nullable Runnable onFail)
    {
        this(scheduler, saveFile, worldName, DEFAULT_CONFIGURATION_NAME, onFail);
    }

    public ConfiguredWorldComponent(@Nonnull ScheduledExecutorService scheduler, @Nonnull File saveFile, @Nonnull String worldName, @Nonnull String configurationName, @Nullable Runnable onFail)
    {
        super(scheduler, saveFile, worldName, onFail);
        configuration = new CompletableFuture<>();
        getWorld(world ->
        {
            File worldFile = WorldUtil.getWorldFile(world);
            File configurationFile = FileUtil.newFileIgnoreCase(worldFile, configurationName + ".yml");

            if (!configurationFile.exists())
            {
                Log.infof("World %s does not have a mapdata.yml file!", worldFile.getName());
                configuration.complete(new YamlConfiguration());
            }
            else
                configuration.complete(YamlConfiguration.loadConfiguration(configurationFile));

        });
    }

    @Override
    public void getConfig(Consumer<YamlConfiguration> callback)
    {
        configuration.thenAccept(callback);
    }


}
