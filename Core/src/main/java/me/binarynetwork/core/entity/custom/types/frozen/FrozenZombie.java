package me.binarynetwork.core.entity.custom.types.frozen;

import me.binarynetwork.core.entity.custom.base.CustomZombie;
import org.bukkit.World;

/**
 * Created by Bench on 9/24/2016.
 */
public class FrozenZombie extends CustomZombie {
    public FrozenZombie(World world)
    {
        super(world);
    }

    @Override
    public void move(double d0, double d1, double d2)
    {
    }

    @Override
    public void burn()
    {
    }
}
