package me.binarynetwork.core.currency;

import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandWrapper;
import me.binarynetwork.core.permissions.PermissionManager;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/9/2016.
 */
public class CurrencyManager {

    private CurrencyDataStorage currencyDataStorage;

    public CurrencyManager(ScheduledExecutorService scheduler, AccountManager accountManager, PermissionManager permissionManager, CommandWrapper commandWrapper)
    {
        this.currencyDataStorage = new CurrencyDataStorage(scheduler, accountManager);
        for (Currency currency : Currency.values())
        {
            commandWrapper.addCommand(new CurrencyCommand(currency, this, permissionManager), currency.getDisplayName(), currency.toString(), currency.getId());
        }
    }

    public void setCurrency(Player player, Currency currency, int amount, Runnable callback)
    {
        getCurrencyDataStorage().get(player, currencyIntegerMap -> {
            currencyIntegerMap.put(currency, amount);
            callback.run();
        });
    }

    public void getCurrency(Player player, Currency currency, Consumer<Integer> callback)
    {
        getCurrencyDataStorage().get(player, currencyIntegerMap -> {
            callback.accept(currencyIntegerMap.getOrDefault(currency, 0));
        });
    }

    private CurrencyDataStorage getCurrencyDataStorage()
    {
        return currencyDataStorage;
    }
}
