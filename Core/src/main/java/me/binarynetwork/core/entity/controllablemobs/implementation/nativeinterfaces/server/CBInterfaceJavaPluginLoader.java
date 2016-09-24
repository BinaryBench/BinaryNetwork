package me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.server;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPluginLoader;

import me.binarynetwork.core.entity.controllablemobs.implementation.nativeinterfaces.primitives.NativeFieldObject;

public final class CBInterfaceJavaPluginLoader {
	public NativeFieldObject<JavaPluginLoader,Server> FIELD_SERVER = new NativeFieldObject<JavaPluginLoader,Server>(JavaPluginLoader.class, "server");

}
