package me.binarynetwork.core.database;

import me.binarynetwork.core.account.Account;
import me.binarynetwork.core.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.util.function.Consumer;

/**
 * Created by Bench on 9/4/2016.
 */
public abstract class PlayerDataStorage<V> extends DataStorage<Account, V> {

    private AccountManager accountManager;

    public PlayerDataStorage(DataSource dataSource, AccountManager accountManager, boolean loadOnJoin)
    {
        super(dataSource);
        this.accountManager = accountManager;
        if (loadOnJoin)
            getAccountManager().addListener(this);
    }

    public void get(Player player, Consumer<V> callback)
    {
        getAccountManager().get(player.getUniqueId(), account ->
        {
            get(account, callback);
        });
    }

    public AccountManager getAccountManager()
    {
        return accountManager;
    }
}
