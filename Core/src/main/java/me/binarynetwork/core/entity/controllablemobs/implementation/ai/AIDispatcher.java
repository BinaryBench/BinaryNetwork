package me.binarynetwork.core.entity.controllablemobs.implementation.ai;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.LivingEntity;

import me.binarynetwork.core.entity.controllablemobs.api.ai.AIPart;
import me.binarynetwork.core.entity.controllablemobs.api.ai.AIType;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AIBehavior;
import me.binarynetwork.core.entity.controllablemobs.api.ai.behaviors.AITargetBehavior;
import me.binarynetwork.core.entity.controllablemobs.implementation.CraftControllableMob;

public class AIDispatcher<E extends LivingEntity> {
	private AIController<E> goalController;
	private AIController<E> targetController;
	
	public AIDispatcher(CraftControllableMob<E> mob) {
		this.goalController = new AIGoalController<E>(mob);
		this.targetController = new AITargetController<E>(mob);
	}
	
	public <B extends AIBehavior<? super E>> AIPart<E,B> add(B behavior) throws IllegalArgumentException {
		if(isTarget(behavior)) return this.targetController.add(behavior);
		else return this.goalController.add(behavior);
	}
	
	public void remove(AIType[] types, boolean remove) {
		Set<AIType> typeSet = new HashSet<AIType>();
		Collections.addAll(typeSet, types);
		this.goalController.remove(typeSet, remove);
		this.targetController.remove(typeSet, remove);
	}
	
	public boolean contains(AIType type) {
		if(this.goalController.contains(type)) return true;
		if(this.targetController.contains(type)) return true;
		return false;
	}
	
	public boolean contains(AIType... types) {
		for(AIType type: types) {
			if(this.goalController.contains(type)) return true;
			if(this.targetController.contains(type)) return true;
		}
		return false;
	}
	
	public void get(List<AIPart<E,?>> list, AIType[] types) {
		Set<AIType> typeSet = null;
		if(types!=null) {
			typeSet = new HashSet<AIType>();
			Collections.addAll(typeSet, types);
		}
		this.goalController.get(list, typeSet);
		this.targetController.get(list, typeSet);
	}
	
	public void clear() {
		this.goalController.clear();
		this.targetController.clear();
	}
	
	public void reset() {
		this.goalController.reset();
		this.targetController.reset();
	}
	
	private boolean isTarget(AIBehavior<? super E> behavior) {
		return behavior instanceof AITargetBehavior;
	}
	
	public void dispose() {
		this.targetController.dispose();
		this.goalController.dispose();
		this.targetController = null;
		this.goalController = null;
	}

}
