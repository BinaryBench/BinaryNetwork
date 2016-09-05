package me.binarynetwork.core.account;

import java.util.UUID;

/**
 * Created by Bench on 9/3/2016.
 */
public class Account {
    private int id;
    private UUID uuid;

    public Account(int id, UUID uuid)
    {
        this.id = id;
        this.uuid = uuid;
    }

    public int getId()
    {
        return id;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public String toString()
    {
        return "Account(id=" + getId() + ", uuid=" + getUUID() + ")";
    }
}
