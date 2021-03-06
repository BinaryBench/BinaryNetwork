package me.binarynetwork.core.entity.controllablemobs.implementation.ai;

import net.minecraft.server.v1_8_R3.PathfinderGoal;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;

public interface AIComponentListener<T extends PathfinderGoal> {

	public void onAdd(CraftControllableMob<?> mob, T goal);
	public void onRemoved(CraftControllableMob<?> mob, T goal);
	
}
