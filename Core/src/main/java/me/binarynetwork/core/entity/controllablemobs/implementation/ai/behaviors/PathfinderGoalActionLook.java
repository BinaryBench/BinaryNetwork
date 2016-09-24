package me.binarynetwork.core.entity.controllablemobs.implementation.ai.behaviors;

import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;
import me.binarynetwork.core.entity.controllablemobs.implementation.actions.ControllableMobActionLook;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.NativeInterfaces;

public class PathfinderGoalActionLook extends PathfinderGoalActionBase<ControllableMobActionLook> {

	public PathfinderGoalActionLook(CraftControllableMob<?> mob) {
		super(mob, ActionType.LOOK);
		this.setMutexBits(1);
	}
	
	@Override
	protected boolean isActionBlocked() {
		return action.getWorld()!=this.mob.nmsEntity.world;
	}

	@Override
	protected boolean canContinueAction() {
		return true;
	}

	@Override
	protected void onTickAction() {
		NativeInterfaces.CONTROLLERLOOK.METHOD_LOOKATCOORDINATES.invoke(this.mob.nmsEntity.getControllerLook(), this.action.getX(), this.action.getY(), this.action.getZ(), 10.0F, NativeInterfaces.ENTITYINSENTIENT.METHOD_GETVERTICALHEADSPEED.invoke(this.mob.nmsEntity));
	}

	

}
