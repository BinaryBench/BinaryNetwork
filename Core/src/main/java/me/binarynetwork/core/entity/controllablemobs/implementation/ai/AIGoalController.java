package me.binarynetwork.core.entity.controllablemobs.implementation.ai;

import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionJump;
import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionLook;
import org.bukkit.entity.LivingEntity;

import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionMoveAttacking;
import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionFollow;
import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionMove;
import me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors.PathfinderGoalActionWait;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.NativeInterfaces;

class AIGoalController<E extends LivingEntity> extends AIController<E> {

	public AIGoalController(CraftControllableMob<E> mob) {
		super(mob, NativeInterfaces.ENTITYINSENTIENT.FIELD_GOALSELECTOR);
	}

	@Override
	protected void createActionGoals() {
		this.addActionGoal(-3, new PathfinderGoalActionMove(mob));
		this.addActionGoal(-2, new PathfinderGoalActionMoveAttacking(mob));
		this.addActionGoal(-1, new PathfinderGoalActionFollow(mob));
		this.addActionGoal(0, new PathfinderGoalActionWait(mob));
		this.addActionGoal(Integer.MAX_VALUE, new PathfinderGoalActionJump(mob));
		this.addActionGoal(Integer.MAX_VALUE, new PathfinderGoalActionLook(mob));
	}

}
