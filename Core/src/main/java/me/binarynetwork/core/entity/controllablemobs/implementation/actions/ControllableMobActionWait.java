package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;

public class ControllableMobActionWait extends ControllableMobActionBase {
	public final int ticks;

	public ControllableMobActionWait(final ControllableMobActionManager manager, final int ticks) {
		super(manager, ActionType.WAIT);
		this.ticks = ticks;
	}

}
