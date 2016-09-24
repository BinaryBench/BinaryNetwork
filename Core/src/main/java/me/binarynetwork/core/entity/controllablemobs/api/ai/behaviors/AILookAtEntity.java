package me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

import me.binarynetwork.core.entity.controllablemobs.api.ai.AIType;
import me.binarynetwork.core.entity.controllablemobs.implementation.ControllableMobHelper;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * The entity will look at another entity when the distance between them is not too long.
 * Usually looks at players, but the corresponding settings can be changed.
 * 
 * @author Cybran
 * @version v4
 *
 */
public class AILookAtEntity extends AIBehavior<LivingEntity> {

	private float lookChance;
	private float maxDistance;
	private Class<? extends Entity> entityClass;
	
	/**
	 * Create with an automatically given priority.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 */
	public AILookAtEntity() {
		this(null, null, null, null);
	}
	
	/**
	 * Create with a custom priority.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param priority the priority of this behavior. Specify 0 to auto-generate it
	 */
	public AILookAtEntity(Integer priority) {
		this(priority, null, null, null);
	}
	
	/**
	 * Create with an automatically given priority but a custom maximum distance.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 */
	public AILookAtEntity(Float maxDistance) {
		this(null, null, maxDistance, null);
	}
	
	/**
	 * Create with an automatically given priority but a custom target entity type.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param entityType the type of entity, this entity will look at
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(EntityType entityType) throws IllegalArgumentException {
		this(null, entityType, null, null);
	}
	
	/**
	 * Create with a custom priority and a custom maximum distance.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param priority the priority of this behavior. Specify 0 to auto-generate it
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 */
	public AILookAtEntity(int priority, float maxDistance) {
		this(priority, null, maxDistance, null);
	}

	/**
	 * Create with a custom priority, a custom maximum distance and a custom target entity type.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 * @param lookChance the chance that the goal will be triggered from 0 to 1
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(Float maxDistance, Float lookChance) throws IllegalArgumentException {
		this(null, null, maxDistance, lookChance);
	}

	/**
	 * Create with a custom priority and a custom target entity type.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param priority the priority of this behavior. Specify 0 to auto-generate it
	 * @param entityType the type of entity, this entity will look at
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(int priority, EntityType entityType) throws IllegalArgumentException {
		this(priority, entityType, null, null);
	}
	
	/**
	 * Create with an automatically given priority but a custom maximum distance and custom target entity type.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param entityType the type of entity, this entity will look at
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(EntityType entityType, Float maxDistance) throws IllegalArgumentException {
		this(null, entityType, maxDistance, null);
	}
	
	/**
	 * Create with a custom priority, a custom maximum distance and a custom target entity type.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param priority the priority of this behavior. Specify 0 to auto-generate it
	 * @param entityType the type of entity, this entity will look at
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(Integer priority, EntityType entityType, Float maxDistance) throws IllegalArgumentException {
		this(priority, entityType, maxDistance, null);
	}

	/**
	 * Create with a custom priority, a custom maximum distance and a custom target entity type.
	 *
	 * @see AILookAtEntity#AILookAtEntity(EntityType, Float, Float)
	 * @param entityType the type of entity, this entity will look at
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 * @param lookChance the chance that the goal will be triggered from 0 to 1
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(EntityType entityType, Float maxDistance, Float lookChance) throws IllegalArgumentException {
		this(null, entityType, maxDistance, lookChance);
	}
	
	/**
	 * Create with a custom priority, a custom maximum distance and a custom target entity type.
	 *
	 * @param priority the priority of this behavior. Specify 0 to auto-generate it
	 * @param entityType the type of entity, this entity will look at
	 * @param maxDistance the maximum distance to look at another entity, in blocks. Default value is 8.0 blocks
	 * @param lookChance the chance that the goal will be triggered from 0 to 1
	 * @throws IllegalArgumentException when the entityType is null or not pointing at a valid entity class
	 */
	public AILookAtEntity(@Nullable Integer priority, @Nullable EntityType entityType, @Nullable Float maxDistance, @Nullable Float lookChance) throws IllegalArgumentException {
		super(priority == null ? 0 : priority);
		this.entityClass = entityType == null ? EntityHuman.class : ControllableMobHelper.getNmsEntityClass(entityType);
		this.maxDistance = maxDistance == null ? 8.0F : maxDistance;
		this.lookChance = lookChance == null ? 0.02F : lookChance;
	}

	@Override
	public PathfinderGoal createPathfinderGoal(CraftControllableMob<?> mob) {
		return new PathfinderGoalLookAtPlayer(mob.nmsEntity, this.entityClass, this.maxDistance, this.lookChance);
	}

	@Override
	public AIType getType() {
		return AIType.ACTION_ENTITYLOOK;
	}

}
