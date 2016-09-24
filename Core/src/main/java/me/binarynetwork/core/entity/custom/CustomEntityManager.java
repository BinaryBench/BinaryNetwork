package me.binarynetwork.core.entity.custom;

import me.binarynetwork.core.common.utils.ReflectionUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bench on 9/24/2016.
 */
@SuppressWarnings("unchecked")
public class CustomEntityManager {

    private static Set<Class<? extends Entity>> knownTypes = new HashSet<>();

    //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
    private static Map<String, Class<? extends Entity>> c;
    private static Map<Class<? extends Entity>, String> d;
    //private static Map<Integer, Class<? extends Entity>> e;
    private static Map<Class<? extends Entity>, Integer> f;
    //private static Map<String, Integer> g;

    static
    {
        try
        {
            c = (Map<String, Class<? extends Entity>>) ReflectionUtil.getPrivateField("c", EntityTypes.class, null);
            d = (Map<Class<? extends Entity>, String>) ReflectionUtil.getPrivateField("d", EntityTypes.class, null);
            //e = (Map<Integer, Class<? extends Entity>>) ReflectionUtil.getPrivateField("e", EntityTypes.class, null);
            f = (Map<Class<? extends Entity>, Integer>) ReflectionUtil.getPrivateField("f", EntityTypes.class, null);
            //g = (Map<String, Integer>) ReflectionUtil.getPrivateField("g", EntityTypes.class, null);
        }
        catch (NoSuchFieldException | ClassCastException e)
        {
            e.printStackTrace();
            ServerUtil.shutdown("Unable to get EntityTypes");
        }
    }

    public static <T extends Entity> T spawnEntity(World world, Location loc, T entity)
    {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        ((CraftWorld) world).getHandle().addEntity(entity);
        return entity;
    }

    public static void registerEntity(Class<? extends Entity> clazz, Class<? extends Entity> extendedFrom)
    {
        int id = f.get(extendedFrom);
        String name = d.get(extendedFrom);
        registerEntity(clazz, name, id);
    }

    public static void registerEntity(Class<? extends Entity> clazz, String name, int id)
    {
        if (knownTypes.contains(clazz))
            return;
        c.put(name, clazz);
        d.put(clazz, name);
        //e.put(id, clazz);
        f.put(clazz, id);
        //g.put(name, id);
        knownTypes.add(clazz);
    }
}
