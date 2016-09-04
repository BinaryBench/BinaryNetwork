package me.binarynetwork.core.account;

import java.util.UUID;

/**
 * Created by Bench on 9/3/2016.
 */
public class Account {
    private int id;
    private UUID playersUUID;

    public Account(int id, UUID playersUUID)
    {
        this.id = id;
        this.playersUUID = playersUUID;
    }

    public int getId()
    {
        return id;
    }

    public UUID getPlayersUUID()
    {
        return playersUUID;
    }
}
