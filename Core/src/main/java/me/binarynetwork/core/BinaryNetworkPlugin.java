package me.binarynetwork.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandManager;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.currency.CurrencyDataStorage;
import me.binarynetwork.core.currency.CurrencyManager;
import me.binarynetwork.core.permissions.PermissionManager;
import me.binarynetwork.core.permissions.RankDataStorage;
import me.binarynetwork.core.permissions.RankManager;
import me.binarynetwork.core.permissions.RankPermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Bench on 8/31/2016.
 */
public abstract class BinaryNetworkPlugin extends JavaPlugin implements Listener{

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

    private SimpleComponentWrapper componentWrapper;

    private ProtocolManager protocolManager;
    private ScheduledExecutorService scheduler;
    private AccountManager accountManager;
    private CurrencyManager currencyManager;
    private CommandManager commandManager;

    @Override
    public final void onEnable()
    {
        plugin = this;
        getDataFolder().mkdirs();
        //Scheduler
        scheduler = Executors.newScheduledThreadPool(10);

        //Protocol
        protocolManager = ProtocolLibrary.getProtocolManager();



        //Command
        commandManager = new CommandManager(protocolManager);

        //ComponentWrapper
        this.componentWrapper = new SimpleComponentWrapper();


        //DataStorage
        accountManager = new AccountManager(scheduler);
        RankManager rankManager = new RankManager(getScheduler(), accountManager);

        PermissionManager permissionManager = new RankPermissionManager(rankManager, componentWrapper);

        currencyManager = new CurrencyManager(scheduler, accountManager, permissionManager, commandManager);




        new RankDataStorage(getScheduler(), getAccountManager());




        //new RankPermissionManager(componentWrapper);

        registerEvents(this);

        WorldUtil.purgeTemporaryWorlds();
        componentWrapper.enable();
        enable();
    }


    @EventHandler
    public void prelogin(AsyncPlayerPreLoginEvent event)
    {

    }

    @Override
    public final void onDisable()
    {
        disable();
        componentWrapper.disable();
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

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    public SimpleComponentWrapper getComponentWrapper()
    {
        return componentWrapper;
    }

    public ProtocolManager getProtocolManager()
    {
        return protocolManager;
    }
}
