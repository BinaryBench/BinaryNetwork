package me.binarynetwork.core.entity;

import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.CustomEntityManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.time.LocalTime;

/**
 * Created by Bench on 9/24/2016.
 */
public class EntityManager {

    public static Entity spawnEntity(Location loc, EntityType entityType)
    {
        return spawnEntity(loc.getWorld(), loc, entityType);
    }

    public static Entity spawnEntity(World world, Location loc, EntityType entityType)
    {
        return world.spawnEntity(loc, entityType);
    }

    public static <T extends Entity> T spawn(Location loc, Class<T> clazz)
    {
        return spawn(loc.getWorld(), loc, clazz);
    }

    public static <T extends Entity> T spawn(World world, Location loc, Class<T> clazz)
    {
        return world.spawn(loc, clazz);
    }


    public static <T extends net.minecraft.server.v1_8_R3.Entity> T spawnCustom(Location loc, T entity)
    {
        return spawnCustom(loc.getWorld(), loc, entity);
    }
    public static <T extends net.minecraft.server.v1_8_R3.Entity> T spawnCustom(World world, Location loc, T entity)
    {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        ((CraftWorld) world).getHandle().addEntity(entity);
        return entity;
    }




    public static Arrow spawnArrow(Location loc, Vector direction, float speed, float spread)
    {
        return spawnArrow(loc.getWorld(), loc, direction, speed, spread);
    }
    public static Arrow spawnArrow(World world, Location loc, Vector direction, float speed, float spread)
    {
        return world.spawnArrow(loc, direction, speed, spread);
    }

    public static FallingBlock spawnFallingBlock(Location loc, Material material)
    {
        return spawnFallingBlock(loc.getWorld(), loc, material);
    }
    public static FallingBlock spawnFallingBlock(Location loc, MaterialData materialData)
    {
        return spawnFallingBlock(loc.getWorld(), loc, materialData);
    }
    public static FallingBlock spawnFallingBlock(World world, Location loc, Material material)
    {
        return spawnFallingBlock(world, loc, new MaterialData(material));
    }

    @SuppressWarnings("deprecation")
    public static FallingBlock spawnFallingBlock(World world, Location loc, MaterialData materialData)
    {
        return world.spawnFallingBlock(loc, materialData.getItemType(), materialData.getData());
    }

}
