package me.binarynetwork.core.fixes;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/17/2016.
 */
@SuppressWarnings("deprecated")
public class WorldMemoryFix {
    private static final Class<World> worldServerClass = World.class;
    private static final Class<ChunkProviderServer> chunkProviderServerClass = ChunkProviderServer.class;
    private static final Class<Chunk> chunkClass = Chunk.class;
    private static final Class<TileEntity> tileEntityClass = TileEntity.class;

    // ChunkProviderServer
    private static Field chunkProvider;
    private static Field unloadQueue, chunks, worldServerCPS;

    // Chunk
    private static Field tileEntities, bukkitChunk, worldChunk;

    // TileEntity
    private static Field worldTileEntity;

    static {
        try
        {
            chunkProvider  = worldServerClass.getDeclaredField("chunkProvider");

            unloadQueue    = chunkProviderServerClass.getDeclaredField("unloadQueue");
            chunks         = chunkProviderServerClass.getDeclaredField("chunks");
            worldServerCPS = chunkProviderServerClass.getDeclaredField("world");

            tileEntities   = chunkClass.getDeclaredField("tileEntities");
            bukkitChunk    = chunkClass.getDeclaredField("bukkitChunk");
            worldChunk     = chunkClass.getDeclaredField("world");

            worldTileEntity = tileEntityClass.getDeclaredField("world");


            unloadQueue.setAccessible(true);
            chunks.setAccessible(true);
            worldServerCPS.setAccessible(true);

            tileEntities.setAccessible(true);
            bukkitChunk.setAccessible(true);
            worldChunk.setAccessible(true);

            worldTileEntity.setAccessible(true);

        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean unload(org.bukkit.World world, ScheduledExecutorService scheduler, boolean save, Consumer<Boolean> callback)
    {
        ChunkProviderServer chunkProviderServer = ((CraftWorld) world).getHandle().chunkProviderServer;

        //for (org.bukkit.Chunk chunk : world.getLoadedChunks())
            //world.unloadChunk(chunk.getX(), chunk.getZ(), save, false);


        if (!Bukkit.unloadWorld(world, save))
        {
            callback.accept(false);
            return false;
        }

        scheduler.schedule(() ->
        {
            try
            {
                clearChunkPS(chunkProviderServer);
                callback.accept(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                callback.accept(false);
            }

        }, 10, TimeUnit.SECONDS);

        return true;
    }

    private static void clearChunkPS(ChunkProviderServer chunkPS) throws Exception
    {
        if (chunkPS == null) return;

        unloadQueue.set(chunkPS, null);
        LongObjectHashMap<Chunk> chunkMap = (LongObjectHashMap<Chunk>) chunks.get(chunkPS);

        Set<Map.Entry<Long, Chunk>> entries = chunkMap.entrySet();

        for (Map.Entry<Long, Chunk> entry : entries)
        {
            Chunk chunk = entry.getValue();

            chunkMap.remove(entry.getKey().longValue());

            worldChunk.set(chunk, null);

            Map<BlockPosition, TileEntity> tileEntityMap = (Map<BlockPosition, TileEntity>) tileEntities.get(chunk);
            for (Map.Entry<BlockPosition, TileEntity> entityEntry : tileEntityMap.entrySet())
                worldTileEntity.set(entityEntry.getValue(), null);

            tileEntities.set(chunk, null);

            //org.bukkit.Chunk bukkitC = (org.bukkit.Chunk) bukkitChunk.get(chunk);
            bukkitChunk.set(chunk, null);
        }

        chunks.set(chunkPS, null);
        worldServerCPS.set(chunkPS, null);

    }

}
