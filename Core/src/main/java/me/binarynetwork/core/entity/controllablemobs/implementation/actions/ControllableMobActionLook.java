package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import net.minecraft.server.v1_8_R3.World;
import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;

public abstract class ControllableMobActionLook extends ControllableMobActionBase {

	public ControllableMobActionLook(final ControllableMobActionManager manager) {
		super(manager, ActionType.LOOK);
	}
	
	public abstract double getX();
	public abstract double getY();
	public abstract double getZ();
	public abstract World getWorld();

}
