package me.binarynetwork.core.currency;

import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandWrapper;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/9/2016.
 */
public class CurrencyManager {

    private CurrencyDataCache cache;

    public CurrencyManager(ScheduledExecutorService scheduler, AccountManager accountManager, PermissionManager permissionManager, CommandWrapper commandWrapper)
    {
        this.cache = new CurrencyDataCache(scheduler, accountManager);
        for (Currency currency : Currency.values())
        {
            commandWrapper.addCommand(new CurrencyCommand(currency, this, permissionManager), currency.getDisplayName(), currency.toString(), currency.getId());
        }
    }

    public Integer getCurrency(Player player, Currency currency)
    {
        CurrencyToken token = getCache().getIfExists(player);
        if (token == null)
            return null;
        return token.getCurrency(currency);
    }

    public void getCurrency(Player player, Currency currency, Consumer<Integer> callback)
    {
        getCache().get(player, currencyToken ->
            Scheduler.runSync(() ->
                callback.accept(currencyToken.getCurrency(currency))
            )
        );
    }

    public boolean setCurrency(Player player, Currency currency, int amount)
    {
        CurrencyToken token = getCache().getIfExists(player);
        if (token == null)
            return false;
        token.setCurrency(currency, amount);
        return true;
    }
    public void setCurrency(Player player, Currency currency, int amount, Runnable callback)
    {
        getCache().get(player, currencyToken -> {
            currencyToken.setCurrency(currency, amount);
            Scheduler.runSync(callback);
        });
    }



    public CurrencyDataCache getCache()
    {
        return cache;
    }
}
