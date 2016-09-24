package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;

public class ControllableMobActionJump extends ControllableMobActionBase {
	public int times;

	public ControllableMobActionJump(final ControllableMobActionManager manager, final int times) {
		super(manager, ActionType.JUMP);
		this.times = times;
	}

	@Override
	public boolean isValid() {
		return super.isValid() && this.times>0;
	}

}
