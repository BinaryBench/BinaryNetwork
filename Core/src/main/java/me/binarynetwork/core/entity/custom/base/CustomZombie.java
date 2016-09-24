package me.binarynetwork.core.entity.custom.base;

import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.CustomEntityManager;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

/**
 * Created by Bench on 9/24/2016.
 */
public class CustomZombie extends EntityZombie implements CustomEntity {

    public CustomZombie(World world)
    {
        super(((CraftWorld)world).getHandle());
        CustomEntityManager.registerEntity(getClass(), EntityZombie.class);
    }

    @Override
    public void collide(Entity entity)
    {
        this.collide(entity.getBukkitEntity());
    }

    public void collide(org.bukkit.entity.Entity entity)
    {
        super.collide(((CraftEntity) entity).getHandle());
    }
}
