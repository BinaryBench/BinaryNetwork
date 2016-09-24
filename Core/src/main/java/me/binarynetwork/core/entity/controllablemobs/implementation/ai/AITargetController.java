package me.binarynetwork.core.entity.controllablemobs.implementation.ai;

import org.bukkit.entity.LivingEntity;

import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionTarget;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.NativeInterfaces;

class AITargetController<E extends LivingEntity> extends AIController<E> {

	public AITargetController(CraftControllableMob<E> mob) {
		super(mob, NativeInterfaces.ENTITYINSENTIENT.FIELD_TARGETSELECTOR);
	}

	@Override
	protected void createActionGoals() {
		this.addActionGoal(-1, new PathfinderGoalActionTarget(mob));
	}

}
