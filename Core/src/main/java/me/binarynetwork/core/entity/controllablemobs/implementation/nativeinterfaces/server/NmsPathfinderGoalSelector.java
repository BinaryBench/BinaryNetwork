package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server;

import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.primitives.NativeFieldObject;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.system.NativeMethodPublic;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

public final class NmsPathfinderGoalSelector {
	public final NativeFieldObject<PathfinderGoalSelector, UnsafeList<Object>> FIELD_GOALITEMS = new NativeFieldObject<PathfinderGoalSelector, UnsafeList<Object>>(PathfinderGoalSelector.class, "b");
	public final NativeFieldObject<PathfinderGoalSelector, UnsafeList<Object>> FIELD_ACTIVEGOALITEMS = new NativeFieldObject<PathfinderGoalSelector, UnsafeList<Object>>(PathfinderGoalSelector.class, "c");
	public final AddPathfinderGoal METHOD_ADDPATHFINDERGOAL = new AddPathfinderGoal();
	
	public class AddPathfinderGoal extends NativeMethodPublic {
		public void invoke(PathfinderGoalSelector selector, int priority, PathfinderGoal goal) {
			try {
				selector.a(priority, goal);
			} catch(Throwable e) {
				this.handleException(e);
			}
		}
	}

}
