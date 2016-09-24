package me.binarynetwork.core.entity.custom.base;

import me.binarynetwork.core.entity.custom.CustomEntity;
import me.binarynetwork.core.entity.custom.CustomEntityManager;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.World;

/**
 * Created by Bench on 9/24/2016.
 */
public class CustomSlime extends EntitySlime implements CustomEntity {
    public CustomSlime(World world)
    {
        super(world);
        CustomEntityManager.registerEntity(getClass(), EntitySlime.class);
    }
}
