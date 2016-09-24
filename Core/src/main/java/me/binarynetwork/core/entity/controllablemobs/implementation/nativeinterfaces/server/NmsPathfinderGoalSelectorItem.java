package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server;

import net.minecraft.server.v1_8_R3.PathfinderGoal;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.primitives.NativeFieldInt;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.primitives.NativeFieldObject;

public final class NmsPathfinderGoalSelectorItem {
	public final NativeFieldObject<Object, PathfinderGoal> FIELD_GOAL = new NativeFieldObject<Object, PathfinderGoal>("a");
	public final NativeFieldInt<Object> FIELD_PRIORITY = new NativeFieldInt<Object>("b");

}
