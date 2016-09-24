package me.binarynetwork.core.entity.custom;


import net.minecraft.server.v1_8_R3.Entity;

/**
 * Created by Bench on 9/24/2016.
 */
public interface CustomEntity  {

    default Entity getEntity()
    {
        if (this instanceof Entity)
            return (Entity) this;
        throw new IllegalStateException(String.format("Class %s does not implement %s", getClass().getName(), Entity.class.getName()));
    }

    default org.bukkit.entity.Entity getBukkitEntity()
    {
        return getEntity().getBukkitEntity();
    }
}
