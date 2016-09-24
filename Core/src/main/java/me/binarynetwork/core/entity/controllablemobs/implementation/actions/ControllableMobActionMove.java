package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import org.bukkit.Location;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;

public class ControllableMobActionMove extends ControllableMobActionMoveAbstract {
	
	public ControllableMobActionMove(ControllableMobActionManager manager, Location to, double movementSpeedMultiplicator) {
		super(manager, ActionType.MOVE, to, movementSpeedMultiplicator);
	}

}
