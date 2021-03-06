package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NavigationAbstract;
import net.minecraft.server.v1_8_R3.PathEntity;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.system.NativeMethodPublic;

public class NmsNavigation {
	// set in constructor, 3rd argument for world.findPath
	//public final NativeFieldFloat<Navigation> FIELD_MAXPATHLENGTH = new NativeFieldFloat<Navigation>(Navigation.class, "e");
	public final IsMoveFinished METHOD_ISNOTMOVING = new IsMoveFinished();
	public final StopMove METHOD_STOP = new StopMove();
	public final MoveToEntity METHOD_MOVETOENTITY = new MoveToEntity();
	public final MoveAlongPath METHOD_MOVEALONGPATH = new MoveAlongPath();
	public final CreatePathToEntity METHOD_CREATEPATHTOENTITY = new CreatePathToEntity();
	public final CreatePathToLocation METHOD_CREATEPATHTOLOCATION = new CreatePathToLocation();
	// first of 4 booleans
	//public final NativeFieldBoolean<NmsNavigation> FIELD_USEOPENDOOR = new NativeFieldBoolean<NmsNavigation>(NmsNavigation.class, "f");
	// second of 4 booleans
	//public final NativeFieldBoolean<NmsNavigation> FIELD_USECLOSEDDOOR = new NativeFieldBoolean<NmsNavigation>(NmsNavigation.class, "f");
	// third of 4 booleans
	//public final NativeFieldBoolean<NmsNavigation> FIELD_AVOIDWATER = new NativeFieldBoolean<NmsNavigation>(NmsNavigation.class, "f");
	// last of 4 booleans
	//public final NativeFieldBoolean<NmsNavigation> FIELD_CANSWIM = new NativeFieldBoolean<NmsNavigation>(NmsNavigation.class, "f");

	public class IsMoveFinished extends NativeMethodPublic {
		public boolean invoke(final NavigationAbstract navigation) {
			try {
				// return (this.c == null) || (this.c.b());
				return navigation.m();
			} catch(Throwable e) {
				this.handleException(e);
				return true;
			}
		}
	}
	
	public class StopMove extends NativeMethodPublic {
		public void invoke(final NavigationAbstract navigation) {
			try {
				// this.c = null;
				navigation.k();
			} catch(Throwable e) {
				this.handleException(e);
			}
		}
	}
	
	public class MoveToEntity extends NativeMethodPublic {
		public void invoke(final NavigationAbstract navigation, EntityLiving entity, double movementSpeedMultiplicator) {
			try {
				navigation.a(entity, movementSpeedMultiplicator);
			} catch(Throwable e) {
				this.handleException(e);
			}
		}
	}
	
	public class MoveAlongPath extends NativeMethodPublic {
		public void invoke(final NavigationAbstract navigation, PathEntity path, double movementSpeedMultiplicator) {
			try {
				navigation.a(path, movementSpeedMultiplicator);
			} catch(Throwable e) {
				this.handleException(e);
			}
		}
	}
	
	public class CreatePathToEntity extends NativeMethodPublic {
		public PathEntity invoke(final NavigationAbstract navigation, final EntityLiving entity) {
			try {
				return navigation.a(entity);
			} catch(Throwable e) {
				this.handleException(e);
				return null;
			}
		}
	}
	
	public class CreatePathToLocation extends NativeMethodPublic {
		public PathEntity invoke(final NavigationAbstract navigation, final double x, final double y, final double z) {
			try {
				return navigation.a(x,y,z);
			} catch(Throwable e) {
				this.handleException(e);
				return null;
			}
		}
	}	
	
}
