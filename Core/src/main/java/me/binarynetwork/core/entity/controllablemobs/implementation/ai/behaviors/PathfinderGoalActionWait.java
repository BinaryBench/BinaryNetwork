package me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.actions.ControllableMobActionWait;

public class PathfinderGoalActionWait extends PathfinderGoalActionBase<ControllableMobActionWait> {
	private int ticks;

	public PathfinderGoalActionWait(CraftControllableMob<?> mob) {
		super(mob, ActionType.WAIT);
	}

	@Override
	protected void onStartAction() {
		this.ticks = this.action.ticks;
	}

	@Override
	protected boolean canContinueAction() {
		return this.ticks>0;
	}

	@Override
	protected void onTickAction() {
		this.ticks--;
	}

}
