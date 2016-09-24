package me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.actions.ControllableMobActionJump;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.NativeInterfaces;

public class PathfinderGoalActionJump extends PathfinderGoalActionBase<ControllableMobActionJump> {

	public PathfinderGoalActionJump(final CraftControllableMob<?> mob) {
		super(mob, ActionType.JUMP);
	}

	@Override
	protected void onStartAction() {
		NativeInterfaces.CONTROLLERJUMP.METHOD_JUMP.invoke(this.mob.nmsEntity.getControllerJump());
	}

	@Override
	protected boolean canContinueAction() {
		return !this.mob.nmsEntity.onGround;
	}

	@Override
	protected void onEndAction() {
		this.action.times--;
	}

}
