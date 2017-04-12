package me.binarynetwork.core.component.world;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.NullableRunnable;
import me.binarynetwork.core.common.utils.*;
import me.binarynetwork.core.component.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/26/2016.
 */
public class WorldComponent extends BaseComponent implements WorldManager {

    private ScheduledExecutorService scheduler;

    private File saveFile;
    private String worldName;
    private World world;
    private Runnable onFail;
    private CompletableFuture<World> completableFuture;

    public WorldComponent(@Nonnull ScheduledExecutorService scheduler, @Nonnull File saveFile, @Nonnull String worldName, @Nullable Runnable onFail)
    {
        this.scheduler = scheduler;
        this.worldName = worldName;
        this.saveFile = saveFile;
        this.onFail = new NullableRunnable(onFail);
        this.completableFuture = new CompletableFuture<>();
        completableFuture.thenAccept(world1 -> Log.debugf("Main thread: %s", Bukkit.isPrimaryThread()));
    }

    @Override
    public void onEnable()
    {
        WorldUtil.deleteWorld(getWorldName(), getScheduler(), (aBoolean) -> {
            if (!aBoolean)
            {
                failure();
                return;
            }

            this.world = WorldUtil.createTemporaryWorld(getSaveFile(), getWorldName());
            if (world != null)
            {
                this.world.setAutoSave(false);
                completableFuture.complete(world);
            }
            else
            {
                failure();
                completableFuture.complete(null);
            }

        });
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().runTaskLater(ServerUtil.getPlugin(), () ->
                WorldUtil.deleteWorld(getWorld(), getScheduler()), 60);
    }


    @Override
    public void getWorld(Consumer<World> callback)
    {
        completableFuture.thenAccept(callback);
    }

    @Nullable
    @Override
    public World get()
    {
        return completableFuture.getNow(null);
    }


    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public File getSaveFile()
    {
        return saveFile;
    }


    public String getWorldName()
    {
        return worldName;
    }

    public World getWorld()
    {
        return world;
    }

    public CompletableFuture<World> getCompletableFuture()
    {
        return completableFuture;
    }

    protected void failure()
    {
        if (onFail != null)
            failure();
    }


}
