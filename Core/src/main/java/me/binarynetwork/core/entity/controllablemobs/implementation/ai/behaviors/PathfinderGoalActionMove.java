package me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.actions.ControllableMobActionMove;

public class PathfinderGoalActionMove extends PathfinderGoalActionMoveAbstract<ControllableMobActionMove> {

	public PathfinderGoalActionMove(CraftControllableMob<?> mob) {
		super(mob, ActionType.MOVE);
	}
	
}
