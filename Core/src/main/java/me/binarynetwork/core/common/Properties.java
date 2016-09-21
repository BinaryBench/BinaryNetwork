package me.binarynetwork.core.common;

import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PropertyManager;

import java.util.HashMap;

/**
 * Created by Bench on 9/20/2016.
 */
public class Properties {

    public static void savePropertiesFile() {
        getPropertyManager().savePropertiesFile();
    }

    public static void setServerProperty(ServerProperty property, Object value) {
        getPropertyManager().setProperty(property.getPropertyName(), value);
    }

    public static int getServerPropertyInt(ServerProperty property, int defaultValue)
    {
        return getPropertyManager().getInt(property.getPropertyName(), defaultValue);
    }

    public static long getServerPropertyLong(ServerProperty property, long defaultValue)
    {
        return getPropertyManager().getLong(property.getPropertyName(), defaultValue);
    }

    public static String getServerPropertyString(ServerProperty property, String defaultValue)
    {
        return getPropertyManager().getString(property.getPropertyName(), defaultValue);
    }

    public static boolean getServerPropertyString(ServerProperty property, boolean defaultValue)
    {
        return getPropertyManager().getBoolean(property.getPropertyName(), defaultValue);
    }

    private static PropertyManager getPropertyManager()
    {
        return ((DedicatedServer) MinecraftServer.getServer()).propertyManager;
    }

    public enum ServerProperty {

        SPAWN_PROTECTION("spawn-protection"),
        SERVER_NAME("server-name"),
        FORCE_GAMEMODE("force-gamemode"),
        NETHER("allow-nether"),
        DEFAULT_GAMEMODE("gamemode"),
        QUERY("enable-query"),
        PLAYER_IDLE_TIMEOUT("player-idle-timeout"),
        DIFFICULTY("difficulty"),
        SPAWN_MONSTERS("spawn-monsters"),
        OP_PERMISSION_LEVEL("op-permission-level"),
        RESOURCE_PACK_HASH("resource-pack-hash"),
        RESOURCE_PACK("resource-pack"),
        ANNOUNCE_PLAYER_ACHIEVEMENTS("announce-player-achievements"),
        PVP("pvp"),
        SNOOPER("snooper-enabled"),
        LEVEL_NAME("level-name"),
        LEVEL_TYPE("level-type"),
        LEVEL_SEED("level-seed"),
        HARDCORE("hardcore"),
        COMMAND_BLOCKS("enable-command-blocks"),
        MAX_PLAYERS("max-players"),
        PACKET_COMPRESSION_LIMIT("network-compression-threshold"),
        MAX_WORLD_SIZE("max-world-size"),
        IP("server-ip"),
        PORT("server-port"),
        DEBUG_MODE("debug"),
        SPAWN_NPCS("spawn-npcs"),
        SPAWN_ANIMALS("spawn-animals"),
        FLIGHT("allow-flight"),
        VIEW_DISTANCE("view-distance"),
        WHITE_LIST("white-list"),
        GENERATE_STRUCTURES("generate-structures"),
        MAX_BUILD_HEIGHT("max-build-height"),
        MOTD("motd"),
        REMOTE_CONTROL("enable-rcon");

        private String propertyName;

        ServerProperty(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }

    }

}