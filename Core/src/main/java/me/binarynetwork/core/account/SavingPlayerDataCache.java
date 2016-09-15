package me.binarynetwork.core.account;

import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.utils.RandomUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/14/2016.
 */
public abstract class SavingPlayerDataCache<V> extends PlayerDataCache<V> {

    private ScheduledFuture<?> task;

    public SavingPlayerDataCache(DataSource dataSource, ScheduledExecutorService scheduler, AccountManager accountManager, boolean loadOnJoin)
    {
        super(dataSource, scheduler, accountManager, loadOnJoin);
        enableAutoSave(1, TimeUnit.MINUTES);
    }

    public abstract void saveData(Connection connection, Map<Account, V> key, Consumer<Integer> successes);

    //Save management
    public boolean enableAutoSave(int amount, TimeUnit unit)
    {
        if (task != null || getScheduler() != null)
            return false;
        task = getScheduler().scheduleWithFixedDelay(this::saveData, (long) ((amount * RandomUtil.getRandom().nextDouble()) % amount), amount, unit);
        return true;
    }

    public boolean disableAutoSave()
    {
        if (task == null)
            return false;
        task.cancel(true);
        task = null;
        return true;
    }

    //Save
    public boolean save(Account key)
    {
        return save(key, aBoolean -> {
            if (aBoolean)
                throw new RuntimeException("Failed to save key: " + key);
        });
    }

    public boolean save(Account key, Consumer<Boolean> success)
    {
        V value = getIfExists(key);
        if (value == null)
            return false;
        execute(connection -> {
            saveData(connection, Collections.singletonMap(key, value), integer -> {
                success.accept(integer < 1);
            });
        });
        return true;
    }

    public void saveData()
    {
        saveData(integer -> {
            if (integer < getCacheSize())
                Log.errorf("Failed to save cache.  Saved %d out of %d", integer, getCacheSize());
        });
    }

    public void saveData(Consumer<Integer> successes)
    {
        execute(connection -> {
            saveData(connection, getCacheAsMap(), successes);
        });
    }

    //AccountListener
    @Override
    public void accountRemoved(Account account)
    {
        save(account);
    }

}
