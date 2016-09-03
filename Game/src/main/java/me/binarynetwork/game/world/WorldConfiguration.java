package me.binarynetwork.game.world;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by Bench on 9/3/2016.
 */
public interface WorldConfiguration {
    YamlConfiguration getConfig(String configName);
    String getSaveName();
}
