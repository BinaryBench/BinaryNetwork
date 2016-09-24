package me.binarynetwork.core.entity.custom.types.frozen;

import me.binarynetwork.core.entity.custom.base.CustomZombie;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.World;
import org.bukkit.event.Listener;

/**
 * Created by Bench on 9/24/2016.
 */
public class FrozenZombie extends CustomZombie implements Listener {
    public FrozenZombie(World world)
    {
        super(world);
    }

    @Override
    public void move(double d0, double d1, double d2)
    {
    }
}
