package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces;

import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.CBInterfaceJavaPluginLoader;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsAttributeModifiable;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsAttributeModifier;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsAttributeRanged;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsControllerJump;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsControllerLook;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsEntity;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsEntityInsentient;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsEntityTypes;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsGenericAttributes;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsIAttribute;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsInterfacePathfinderGoal;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsNavigation;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsPathfinderGoalSelector;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsPathfinderGoalSelectorItem;
import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server.NmsWorld;

public final class NativeInterfaces {
	public static final NmsControllerJump CONTROLLERJUMP = new NmsControllerJump();
	public static final NmsControllerLook CONTROLLERLOOK = new NmsControllerLook();
	public static final NmsEntity ENTITY = new NmsEntity();
	public static final CBInterfaceJavaPluginLoader JAVAPLUGINLOADER = new CBInterfaceJavaPluginLoader();
	public static final NmsNavigation NAVIGATION = new NmsNavigation();
	public static final NmsInterfacePathfinderGoal PATHFINDERGOAL = new NmsInterfacePathfinderGoal();
	public static final NmsPathfinderGoalSelector PATHFINDERGOALSELECTOR = new NmsPathfinderGoalSelector();
	public static final NmsPathfinderGoalSelectorItem PATHFINDERGOALSELECTORITEM = new NmsPathfinderGoalSelectorItem();
	public static final NmsWorld WORLD = new NmsWorld();
	public static final NmsIAttribute IATTRIBUTE = new NmsIAttribute();
	public static final NmsAttributeRanged ATTRIBUTERANGED = new NmsAttributeRanged();
	public static final NmsAttributeModifiable ATTRIBUTEMODIFIABLE = new NmsAttributeModifiable();
	public static final NmsAttributeModifier ATTRIBUTEMODIFIER = new NmsAttributeModifier();
	public static final NmsGenericAttributes GENERICATTRIBUTES = new NmsGenericAttributes();
	public static final NmsEntityInsentient ENTITYINSENTIENT = new NmsEntityInsentient();
	public static final NmsEntityTypes ENTITYTYPES = new NmsEntityTypes();
	
	private NativeInterfaces() {
		throw new AssertionError();
	}

}
