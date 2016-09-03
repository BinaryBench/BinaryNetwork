package me.binarynetwork.core.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Bench on 9/3/2016.
 */
public class LocationUtil {

    private LocationUtil() {
    }

    public static String toLocationDataString(Location loc)
    {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        String str;
        if (yaw == 0 && pitch == 0) {
            str = x + ", " + y + ", " + z;
        } else
            str = x + ", " + y + ", " + z + ", " + yaw + ", " + pitch;
        return str;
    }

    public static String toLocationDataStringRounded(Location loc)
    {
        double x = Math.floor(loc.getX()) + 0.5;
        double y = loc.getY();
        double z = Math.floor(loc.getZ()) + 0.5;
        float yaw = Math.round((loc.getYaw()) / 10) * 10;
        float pitch = Math.round((loc.getPitch()) / 10) * 10;
        String str;
        if (yaw == 0 && pitch == 0) {
            str = x + ", " + y + ", " + z;
        } else
            str = x + ", " + y + ", " + z + ", " + yaw + ", " + pitch;
        return str;
    }

    public static String toStringRounded(Location loc)
    {
        String world = loc.getWorld().getName();
        String str = world + ", " + toLocationDataStringRounded(loc);
        return str;
    }

    public static String toString(Location loc)
    {
        String world = loc.getWorld().getName();
        String str = world + ", " + toLocationDataString(loc);
        return str;
    }

    public static Location toLocation(World world, String str)
    {

        if (world == null) {
            return null;
        }
        if (isLocationData(str)) {
            String[] args = str.replace(" ", "").split(",");
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            float yaw = Float.parseFloat(args[3]);
            float pitch = Float.parseFloat(args[4]);
            return new Location(world, x, y, z, yaw, pitch);
        } else if (isBlockLocationData(str)) {
            String[] args = str.replace(" ", "").split(",");
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            return new Location(world, x, y, z);
        }
        return null;
    }




    public static Location toWorldlessLocation(String str)
    {
        if (isLocationData(str)) {
            String[] args = str.replace(" ", "").split(",");
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            float yaw = Float.parseFloat(args[3]);
            float pitch = Float.parseFloat(args[4]);
            return new Location(null, x, y, z, yaw, pitch);
        } else if (isBlockLocationData(str)) {
            String[] args = str.replace(" ", "").split(",");
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            return new Location(null, x, y, z);
        }
        return null;
    }





    public static Location toLocation(String worldName, String str)
    {
        return toLocation(Bukkit.getWorld(worldName), str);
    }

    public static Location toBlockLocation(World world, String str)
    {
        Location loc = toLocation(world, str);
        if (loc == null)
            return null;
        return toBlockLocation(loc);
    }

    public static Location toBlockLocation(String worldName, String str)
    {
        return toBlockLocation(Bukkit.getWorld(worldName), str);
    }

    public static Location toBlockLocation(Location loc)
    {
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static boolean isAnyLocationData(String str)
    {
        return isLocationData(str) || isBlockLocationData(str);
    }

    public static boolean isLocationData(String str)
    {

        String[] args = str.replace(" ", "").split(",");

        if ((args.length != 5) || (!NumberUtil.isDouble(args[0])) || (!NumberUtil.isDouble(args[1])) || (!NumberUtil.isDouble(args[2]))
                || (!NumberUtil.isFloat(args[3])) || (!NumberUtil.isFloat(args[4])))
            return false;
        return true;
    }

    public static boolean isBlockLocationData(String str)
    {
        String[] args = str.replace(" ", "").split(",");
        if ((args.length != 3) || (!NumberUtil.isDouble(args[0])) || (!NumberUtil.isDouble(args[1])) || (!NumberUtil.isDouble(args[2])))
            return false;
        return true;
    }
}