package me.binarynetwork.core.chat;

import me.binarynetwork.core.common.Log;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bench on 9/15/2016.
 */
public class ChatManager {
    private ConcurrentHashMap<UUID, Long> chatTimes = new ConcurrentHashMap<>();

}
