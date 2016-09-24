package me.binarynetwork.core.entity;

import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.CustomEntityManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.time.LocalTime;

/**
 * Created by Bench on 9/24/2016.
 */
public class EntityManager {

    private static final String NAME_META_TAG = "isPartOfMeta";

    public static Entity spawn(Location loc, EntityType entityType)
    {
        return spawn(loc.getWorld(), loc, entityType);
    }

    public static Entity spawn(World world, Location loc, EntityType entityType)
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


    //Name

    public static void setName(Entity entity, String name)
    {
        ArmorStand namedArmorStand = getNamed(entity);
        if (namedArmorStand != null)
        {
            namedArmorStand.setCustomName(name);
            return;
        }

        ArmorStand armorStand = (ArmorStand) EntityManager.spawn(entity.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);


        Slime slime = (Slime) EntityManager.spawn(entity.getLocation(), EntityType.SLIME);
        slime.setSize(-1);
        slime.setMaximumNoDamageTicks(Integer.MAX_VALUE);
        slime.setNoDamageTicks(Integer.MAX_VALUE);
        slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false));

        slime.setMetadata(NAME_META_TAG, new FixedMetadataValue(ServerUtil.getPlugin(), entity));
        armorStand.setMetadata(NAME_META_TAG, new FixedMetadataValue(ServerUtil.getPlugin(), entity));

        entity.setPassenger(slime);
        slime.setPassenger(armorStand);

        ServerUtil.registerEvents(new  Listener(){

            @EventHandler
            public void onDamage(EntityDamageEvent event)
            {
                if (!event.getEntity().equals(slime))
                    return;
                event.setCancelled(true);
            }

            @EventHandler
            public void onDeath(EntityDeathEvent event)
            {
                if (!event.getEntity().equals(entity))
                    return;
                remove();
            }
            @EventHandler
            public void onDespawn(ItemDespawnEvent event)
            {
                if (!event.getEntity().equals(entity))
                    return;
                remove();
            }
            @EventHandler
            public void dismount(EntityDismountEvent event)
            {
                if (!event.getDismounted().equals(slime) && !event.getDismounted().equals(armorStand))
                    return;
                remove();
            }

            public void remove()
            {
                slime.remove();
                armorStand.remove();
            }
        });
    }

    private static ArmorStand getNamed(Entity entity)
    {
        Entity passenger = entity;
        while ((passenger = passenger.getPassenger()) != null)
        {
            if (passenger.hasMetadata(NAME_META_TAG) && passenger instanceof ArmorStand)
            {
                return (ArmorStand) passenger;
            }
        }
        return null;
    }

    public static String getName(Entity entity)
    {
        ArmorStand named = getNamed(entity);
        if (named == null)
            return null;
        return named.getCustomName();
    }

    public static boolean removeName(Entity entity)
    {
        boolean edited = false;
        Entity passenger = entity;
        while ((passenger = passenger.getPassenger()) != null)
        {
            for (MetadataValue metaValue : passenger.getMetadata(NAME_META_TAG))
            {
                if (metaValue.value() instanceof Entity && metaValue.value().equals(entity))
                {
                    passenger.remove();
                    edited = true;
                }
            }
        }
        return edited;
    }

    public static boolean hasName(Entity entity)
    {
        return getName(entity) != null;
    }

}
