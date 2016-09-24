package me.binarynetwork.core.entity;

import net.minecraft.server.v1_8_R3.Entity;

/**
 * Created by Bench on 9/24/2016.
 */
public class WrappedBinaryEntity implements BinaryEntity {

    private Entity entity;

    public WrappedBinaryEntity(Entity entity)
    {
        this.entity = entity;
    }

    @Override
    public Entity getEntity()
    {
        return this.entity;
    }
}
