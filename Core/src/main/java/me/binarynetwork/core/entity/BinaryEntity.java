package me.binarynetwork.core.entity;

import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

/**
 * Created by Bench on 9/24/2016.
 */
public interface BinaryEntity {
    Entity getEntity();

    default CraftEntity getBukkitEntity()
    {
        return getEntity().getBukkitEntity();
    }
}
