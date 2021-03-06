package me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.actions.ControllableMobActionBase;

abstract class PathfinderGoalActionDelayed<A extends ControllableMobActionBase> extends PathfinderGoalActionBase<A> {
	private int delay;

	public PathfinderGoalActionDelayed(CraftControllableMob<?> mob, ActionType actionType) {
		super(mob, actionType);
	}

	@Override
	protected void onStart() {
		this.delay = this.getDelayTicks();
	}
	
	@Override
	protected boolean canContinue() {
		if(this.isActionBlocked()) {
			this.action.setBlocked();
			return false;
		}
		return this.action.isValid() && (this.delay>0 || this.canContinueAction());
	}

	@Override
	protected void onTick() {
		if(this.delay>0) {
			if(--this.delay==0) this.onStartAction();
		} else {
			this.onTickAction();
		}
	}
	
	protected abstract int getDelayTicks();

}
