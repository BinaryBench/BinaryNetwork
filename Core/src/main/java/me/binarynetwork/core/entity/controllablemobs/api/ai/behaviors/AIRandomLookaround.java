package me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors;

import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import me.binarynetwork.core.entity.controllablemobs.api.ai.AIType;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;

/**
 * If this behavior is added, the entity will curiously look around when it is idle.
 * This won't make the entity move around, it just <i>looks</i> around! See {@link AIRandomStroll} if you want the entity to move around.
 * 
 * @author Cybran
 * @version v4
 *
 */
public class AIRandomLookaround extends AIBehavior<LivingEntity> {
	
	/**
	 * Create with an automatically given priority.
	 */
	public AIRandomLookaround() {
		this(0);
	}

	/**
	 * Create with a custom priority.
	 * 
	 * @param priority the priority of this behavior. Specify 0 to auto-generate it
	 */
	public AIRandomLookaround(final int priority) {
		super(priority);
	}

	@Override
	public PathfinderGoal createPathfinderGoal(final CraftControllableMob<?> mob) {
		return new PathfinderGoalRandomLookaround(mob.nmsEntity);
	}

	@Override
	public AIType getType() {
		return AIType.ACTION_RANDOMLOOK;
	}

}
