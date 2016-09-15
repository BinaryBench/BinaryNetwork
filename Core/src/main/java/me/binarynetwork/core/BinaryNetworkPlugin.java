package me.binarynetwork.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.binarynetwork.core.account.AccountManagerOld;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandManager;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.currency.CurrencyDataCache;
import me.binarynetwork.core.currency.CurrencyManager;
import me.binarynetwork.core.permissions.PermissionManager;
import me.binarynetwork.core.permissions.PermissionManagerWrapper;
import me.binarynetwork.core.permissions.RankManager;
import me.binarynetwork.core.permissions.RankPermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    private RankManager rankManager;
    private RankPermissionManager permissionManager;

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
        accountManager = new AccountManager(getScheduler());

        rankManager = new RankManager(getScheduler(), getAccountManager());
        //PermissionManager
        permissionManager = new RankPermissionManager(getRankManager(), getComponentWrapper());

        currencyManager = new CurrencyManager(getScheduler(), getAccountManager(), getPermissionManager(), getCommandManager());



        registerEvents(this);

        WorldUtil.purgeTemporaryWorlds();
        componentWrapper.enable();
        enable();
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

    public SimpleComponentWrapper getComponentWrapper()
    {
        return componentWrapper;
    }

    public ProtocolManager getProtocolManager()
    {
        return protocolManager;
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public AccountManager getAccountManager()
    {
        return accountManager;
    }

    public CurrencyManager getCurrencyManager()
    {
        return currencyManager;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    public RankManager getRankManager()
    {
        return rankManager;
    }

    public RankPermissionManager getPermissionManager()
    {
        return permissionManager;
    }
}
