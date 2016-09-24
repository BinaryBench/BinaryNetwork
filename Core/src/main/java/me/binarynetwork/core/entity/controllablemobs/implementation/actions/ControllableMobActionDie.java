package me.binarynetwork.core.entity.controllablemobs.implementation.actions;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityLiving;
import me.binarynetwork.core.entity.controllablemobs.api.actions.ActionType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;

public class ControllableMobActionDie extends ControllableMobActionBase {
	private final EntityLiving entity;

	public ControllableMobActionDie(final CraftControllableMob<?> mob) {
		super(mob.getActionManager(), ActionType.DIE);
		this.entity = mob.nmsEntity;
	}

	@Override
	void start() {
		super.start();
		this.entity.damageEntity(DamageSource.GENERIC, this.entity.getHealth()+1);
		this.finish();
	}

}
