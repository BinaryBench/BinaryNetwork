package me.binarynetwork.core;

import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.currency.CurrencyDataStorage;
import me.binarynetwork.core.database.DataSourceManager;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Bench on 8/31/2016.
 */
public abstract class BinaryNetworkPlugin extends JavaPlugin {

    private static BinaryNetworkPlugin plugin;
    public static BinaryNetworkPlugin getPlugin()
    {
        return plugin;
    }
    public static void registerEvents(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
    }
    public static void unregisterEvents(Listener listener)
    {
        HandlerList.unregisterAll(listener);
    }



    private ScheduledExecutorService scheduler;
    private AccountManager accountManager;
    private CurrencyDataStorage currencyDataStorage;
    @Override
    public final void onEnable()
    {
        plugin = this;
        getDataFolder().mkdirs();
        scheduler = Executors.newScheduledThreadPool(10);


        accountManager = new AccountManager(scheduler);

        currencyDataStorage = new CurrencyDataStorage(scheduler, 0.1, accountManager);

        WorldUtil.purgeTemporaryWorlds();
        enable();
    }
    @EventHandler
    public void command(PlayerCommandPreprocessEvent event)
    {

        String[] derpyderparray = event.getMessage().replace("  ", " ").split(" ");
        String[] args = (String[]) ArrayUtils.subarray(derpyderparray, 1, derpyderparray.length);
        if (event.getMessage().startsWith("/coin"))
        {


            currencyDataStorage.get(event.getPlayer(), integerIntegerMap -> {
                integerIntegerMap.put(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
            });
            event.setCancelled(true);
        }
        else if (event.getMessage().startsWith("/cache"))
        {
            event.getPlayer().sendMessage("" + (getAccountManager().getIfExists(event.getPlayer().getUniqueId()) != null));
            event.setCancelled(true);
        }
    }

    @Override
    public final void onDisable()
    {
        disable();
        WorldUtil.purgeTemporaryWorlds();
    }

    public void enable()
    {

    }

    public void disable()
    {

    }




    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public AccountManager getAccountManager()
    {
        return accountManager;
    }
}
