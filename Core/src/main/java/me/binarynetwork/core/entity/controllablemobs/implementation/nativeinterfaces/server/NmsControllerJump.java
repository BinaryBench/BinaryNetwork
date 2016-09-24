package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server;

import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.system.NativeMethodPublic;
import net.minecraft.server.v1_8_R3.ControllerJump;

public class NmsControllerJump {
	public final Jump METHOD_JUMP = new Jump();
	
	public class Jump extends NativeMethodPublic {
		public void invoke(final ControllerJump controller) {
			try {
				controller.a();
			} catch(Throwable e) {
				this.handleException(e);
			}
		}
	}

}
