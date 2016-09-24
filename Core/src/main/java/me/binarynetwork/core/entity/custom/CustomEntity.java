package me.binarynetwork.core.entity.custom;

import me.binarynetwork.core.entity.BinaryEntity;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

/**
 * Created by Bench on 9/24/2016.
 */
public interface CustomEntity extends BinaryEntity {
    @Override
    default Entity getEntity()
    {
        if (this instanceof Entity)
            return (Entity) this;
        throw new IllegalStateException(String.format("Class %s does not implement %s", getClass().getName(), Entity.class.getName()));
    }
}
