package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server;

import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.system.NativeMethodPublic;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public final class NmsInterfacePathfinderGoal {
	public final OnEnd METHOD_ONEND = new OnEnd();
	
	public class OnEnd extends NativeMethodPublic {
		public void invoke(PathfinderGoal goal) {
			try {
				// for instance PathfinderGoalMeleeAttack (this.b.getNavigation().h();)
				goal.d();
			} catch(Throwable e) {
				this.handleException(e);
			}
		}
	}

}
