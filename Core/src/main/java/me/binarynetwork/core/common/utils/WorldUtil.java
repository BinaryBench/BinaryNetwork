package me.binarynetwork.core.common.utils;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.fixes.WorldMemoryFix;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by Bench on 8/30/2016.
 */
public class WorldUtil {
    private WorldUtil() {}

    private static final String TEMP_FILE_NAME = ".temp";

    /**
     * @param worldName The name of the world.
     * @return The file located at {@link Bukkit#getWorldContainer()}/
     *         {@code worldName}
     * @see {@link org.bukkit.Server#getWorld(String)}
     */
    public static File getWorldFile(String worldName)
    {
        return new File(Bukkit.getWorldContainer(), worldName);
    }

    /**
     * @param world the world
     * @return The file located at {@link Bukkit#getWorldContainer()}/
     *         {@code worldName}
     * @see {@link org.bukkit.Server#getWorld(String)}
     */
    public static File getWorldFile(World world)
    {
        return new File(Bukkit.getWorldContainer(), world.getName());
    }

    public static boolean isWorld(File file)
    {
        return new File(file, "level.dat").exists();
    }


    /**
     * @param worldName The name of the world.
     * @return The world named {@code worldName}, null if there is no world
     *         loaded by that name.
     * @see {@link org.bukkit.Server#getWorld(String)}
     */
    public static boolean isWorldLoaded(String worldName)
    {
        return getWorld(worldName) != null;
    }

    /**
     * @param worldName The name of the world.
     * @return The world named {@code worldName}, null if there is no world
     *         loaded by that name.
     * @see {@link org.bukkit.Server#getWorld(String)}
     */
    public static World getWorld(String worldName)
    {
        return Bukkit.getServer().getWorld(worldName);
    }

    /**
     * @param worldName The name of the world.
     * @return {@code true} if there is a named {@code worldName} in the
     *         worlds folder.
     */
    public static boolean hasWorldFile(String worldName)
    {
        return getWorldFile(worldName).exists();
    }

    /**
     *
     * Unload then deletes {@code worldName}.
     *
     * @param worldName
     *              The name of the world to be deleted
     * @param plugin
     *              A enabled plugin.
     * @return {@code true} if the world is successfully unloaded. (Note: this
     *         method will return {@code true} even if it fails to delete the
     *         world files after it is unloaded)
     * @see #deleteWorld(String, ScheduledExecutorService, Plugin, Runnable)
     */
    public static boolean deleteWorld(final String worldName, ScheduledExecutorService executorService, Plugin plugin)
    {
        return deleteWorld(worldName, executorService, plugin, null);
    }

    /**
     *
     * Unload then deletes {@code worldName}.
     *
     * @param worldName
     *              The name of the world to be deleted
     * @param plugin
     *              A enabled plugin.
     * @param runAfter
     *              A runnable that will be executed when the world is
     *              unloaded and deleted
     * @return true if the world is successfully unloaded. (Note: it will
     *         return true even if it fails to delete the world files after it
     *         is unloaded)
     */
    public static boolean deleteWorld(final String worldName, ScheduledExecutorService executorService, Plugin plugin, final Runnable runAfter)
    {
        // final Plugin plugin = ExGame.getPlugin();

        World world = getWorld(worldName);

        if (world == null) {
            System.out.print("The world " + worldName + " is not loaded!");
            File file = getWorldFile(worldName);

            if (!file.exists()) {
                System.out.print("Could not find file at: " + file.getPath() + "  Something can't be deleted if its not there.");
                runAfter.run();
                return true;
            }

            executorService.execute(() -> {
                try {
                    FileUtils.deleteDirectory(file);
                    System.out.print("Deleted the files found at: " + file.getPath() + " though.");

                    if (runAfter != null)
                        Bukkit.getScheduler().runTask(plugin, runAfter);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
        return deleteWorld(world, executorService, plugin, runAfter);
    }

    /**
     *
     * Unload then deletes {@code worldName}.
     *
     * @param world
     *              The name of the world to be deleted
     * @param plugin
     *              A enabled plugin.
     * @return {@code true} if the world is successfully unloaded. (Note: this
     *         method will return {@code true} even if it fails to delete the
     *         world files after it is unloaded)
     * @see #deleteWorld(String, ScheduledExecutorService, Plugin, Runnable)
     */
    public static boolean deleteWorld(World world, ScheduledExecutorService executorService, Plugin plugin)
    {
        return deleteWorld(world, executorService, plugin, null);
    }

    /**
     *
     * Unload then deletes {@code worldName}.
     *
     * @param world
     *              world to be deleted
     * @param plugin
     *              A enabled plugin.
     * @param runAfter
     *              A runnable that will be executed when the world is
     *              unloaded and deleted
     * @return true if the world is successfully unloaded. (Note: it will
     *         return true even if it fails to delete the world files after it
     *         is unloaded)
     */
    public static boolean deleteWorld(final World world, ScheduledExecutorService executorService, Plugin plugin, final Runnable runAfter)
    {
        // final Plugin plugin = ExGame.getPlugin();

        final File file = world.getWorldFolder();

        for (Player player : world.getPlayers())
            player.kickPlayer("Welp, that didn't work...");

        world.setAutoSave(false);

        if (WorldUtil.unloadWorld(world, executorService, false, (aBoolean) -> {
            if (aBoolean)
                Log.infof("Successfully finished unloading %s", world.getName());
            else
                Log.infof("Failed to unload %s", world.getName());

            try {
                FileUtils.deleteDirectory(file);
                System.out.print("Successfully deleted " + world.getName());
                if (runAfter != null)
                    Bukkit.getScheduler().runTask(plugin, runAfter);
            }
            catch (Exception e)
            {
                System.out.print("COULD NOT DELETE " + world.getName() + "!");
                e.printStackTrace();
            }
        })) {
            System.out.print("Successfully started to unload " + world.getName());
        } else {
            System.err.print("Bukkit cowardly refused to unload the world: " + world.getName());
            return false;
        }

        return true;
    }

    /**
     *
     * Copys the world from the {@code srcFile} to the worlds folder, then
     * loads to world.
     *
     * @param scrFile the file the world will be copied from.
     * @param worldName
     *              What to name the world when it's loaded.
     *
     * @return The the world loaded, or null if it fails to load.
     */
    public static World createWorld(File scrFile, String worldName)
    {

        if (!scrFile.exists()) {
            System.err.print("There is no file at: " + scrFile.getPath() + " So it can not be loaded into the server");
            return null;
        } else if (!isWorld(scrFile)) {
            System.err.print("The File at " + scrFile.getPath() + " is not a world file!");
            return null;
        }

        File destFile = new File(Bukkit.getServer().getWorldContainer(), worldName);

        if (destFile.exists()) {
            World currentWorld = WorldUtil.getWorld(worldName);

            if (currentWorld != null) {
                System.err.print("The world " + worldName + " already exists!");
                return null;
            }


            System.out.print("There found a file at: " + destFile.getPath() + ". So we will try to delete it!");
            try {
                FileUtils.deleteDirectory(destFile);
            } catch (IOException e) {
                System.err.print("Unable to delete: " + destFile.getPath() + ".");
                e.printStackTrace();
            }
        }

        copyWorld(scrFile, destFile);

        return Bukkit.createWorld(new WorldCreator(worldName));
    }

    /**
     *
     * Copys a minecraft world from the {@code source} to the {@code target}.
     *
     * @param source the file of the world you'd like to copy
     * @param target the file you'd like to copy to
     *
     * @author ThunderWaffeMC
     */
    public static void copyWorld(File source, File target)
    {
        try
        {

            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat", "playerdata", "stats"));

            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean unloadWorld(World world, ScheduledExecutorService scheduler, boolean save, Consumer<Boolean> callback)
    {
        if (world == null)
            return true;

        for (Player player:world.getPlayers())
            player.kickPlayer("#BlameBukkit");

        if (!WorldMemoryFix.unload(world, scheduler, save, callback))
        {
            System.out.println("Bukkit cowardly refused to unload the world: " + world.getName());
            return false;
        }
        return true;
    }

    //THIS IS A HACK AND SHOULD PROBABLY BE REDONE AT SOME POINT!
    public static World createTemporaryWorld(File srcFile, String worldName)
    {
        World world = WorldUtil.createWorld(srcFile, worldName);
        if (world != null)
        {
            File worldFile = WorldUtil.getWorldFile(world);
            if (worldFile.isDirectory())
            {
                markAsTemp(worldFile);
            }
        }
        return world;
    }

    /**
     * Deletes all temporary worlds.  BE CAREFUL, WORLDS MAY STILL BE IN USE!
     */
    public static void purgeTemporaryWorlds(ScheduledExecutorService scheduler)
    {
        File[] files = Bukkit.getWorldContainer().listFiles();
        if (files == null)
            return;

        List<File> tempFiles = new ArrayList<>();

        for (File file : files)
            if (file.isDirectory() && new File(file, TEMP_FILE_NAME).exists())
                WorldUtil.deleteWorld(file.getName(), scheduler, ServerUtil.getPlugin());
    }

    public static void markAsTemp(File worldFile)
    {
        try
        {
            if (!new File(worldFile, TEMP_FILE_NAME).createNewFile())
            {
                System.err.println("Unable to create temporary file in file " + worldFile);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
