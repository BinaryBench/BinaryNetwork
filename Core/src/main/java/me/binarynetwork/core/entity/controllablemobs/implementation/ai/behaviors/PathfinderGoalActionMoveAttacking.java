package me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.actions.ControllableMobActionMoveAttacking;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.NativeInterfaces;

public class PathfinderGoalActionMoveAttacking extends PathfinderGoalActionMoveAbstract<ControllableMobActionMoveAttacking> {

	public PathfinderGoalActionMoveAttacking(CraftControllableMob<?> mob) {
		super(mob, ActionType.MOVEATTACKING);
	}
	
	@Override
	protected boolean isActionBlocked() {
		if(this.mob.nmsEntity.getGoalTarget()==null) return false;
		double distanceSquared = NativeInterfaces.ENTITY.METHOD_GETDISTANCETOLOCATIONSQUARED.invoke(this.mob.nmsEntity, action.x, action.y, action.z);
		if(distanceSquared > this.action.lastDistanceSquared) return false;
		this.action.lastDistanceSquared = distanceSquared;
		return true;
	}

	@Override
	protected boolean isActionRequired() {
		double distanceSquared = NativeInterfaces.ENTITY.METHOD_GETDISTANCETOLOCATIONSQUARED.invoke(this.mob.nmsEntity, action.x, action.y, action.z);
		if(this.mob.nmsEntity.getGoalTarget()!=null) {
			if(distanceSquared < this.action.lastDistanceSquared) {
				this.action.lastDistanceSquared = distanceSquared;
				return false;
			}
			if(Math.sqrt(distanceSquared) < Math.sqrt(this.action.lastDistanceSquared) + this.action.maxDistraction) {
				return false;
			}
		}
		return true;
	}

}
