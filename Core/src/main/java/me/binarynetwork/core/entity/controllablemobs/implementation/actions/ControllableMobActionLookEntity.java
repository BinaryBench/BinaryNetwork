package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class ControllableMobActionLookEntity extends ControllableMobActionLook {
	private final net.minecraft.server.v1_8_R3.Entity entity;

	public ControllableMobActionLookEntity(final CraftControllableMob<?> mob, final Entity entity) {
		super(mob.getActionManager());
		if(mob.getEntity()==entity) throw new IllegalArgumentException("[ControllableMobsAPI] Let the entity look at itself is not allowed");
		this.entity = entity==null ? null : ((CraftEntity) entity).getHandle();
	}

	@Override
	public double getX() {
		return entity.locX;
	}

	@Override
	public double getY() {
		return entity.locY + entity.getHeadHeight();
	}

	@Override
	public double getZ() {
		return entity.locZ;
	}

	@Override
	public boolean isValid() {
		return super.isValid() && this.entity!=null && this.entity.isAlive();
	}

	@Override
	public World getWorld() {
		return this.entity.world;
	}

}
