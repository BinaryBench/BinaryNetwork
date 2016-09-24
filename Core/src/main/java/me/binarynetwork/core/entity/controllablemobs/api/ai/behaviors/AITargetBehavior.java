package me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors;

import org.bukkit.entity.LivingEntity;

/**
 * Base class for target behaviors
 * 
 * @author Cybran
 * @version 23
 *
 */
public abstract class AITargetBehavior<E extends LivingEntity> extends AIBehavior<E> {

	protected AITargetBehavior(int priority) {
		super(priority);
	}

}
