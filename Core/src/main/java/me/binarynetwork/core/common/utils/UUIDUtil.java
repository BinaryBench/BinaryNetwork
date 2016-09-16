package me.binarynetwork.core.common.utils;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.UUIDFetcher;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Created by Bench on 9/16/2016.
 */
public class UUIDUtil {
    public static boolean isOnline(@Nonnull UUID uuid)
    {
        return uuid.version() == 4;
    }

    private static UUID fetchUUID(@Nonnull String name)
    {
        try
        {
            return UUIDFetcher.getUUIDOf(name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
