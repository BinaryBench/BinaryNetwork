package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import org.bukkit.Location;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;

public class ControllableMobActionMoveAttacking extends ControllableMobActionMoveAbstract {
	public final double maxDistraction;
	public double lastDistanceSquared = Double.MAX_VALUE;
	
	public ControllableMobActionMoveAttacking(ControllableMobActionManager manager, Location to, double movementSpeedMultiplicator, double maxDistraction) {
		super(manager, ActionType.MOVEATTACKING, to, movementSpeedMultiplicator);
		if(this.isValid()) {
			this.maxDistraction = 0;
		} else {
			this.maxDistraction = Math.abs(maxDistraction);
		}
	}

}
