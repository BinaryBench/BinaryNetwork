package me.binarynetwork.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandManager;
import me.binarynetwork.core.command.DummyCommand;
import me.binarynetwork.core.command.SimpleCommandWrapper;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.ComponentWrapper;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoDamage;
import me.binarynetwork.core.currency.CurrencyDataStorage;
import me.binarynetwork.core.permissions.RankPermissionManager;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
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

    private SimpleComponentWrapper componentWrapper;

    private ProtocolManager protocolManager;
    private ScheduledExecutorService scheduler;
    private AccountManager accountManager;
    private CurrencyDataStorage currencyDataStorage;
    private CommandManager commandManager;

    @Override
    public final void onEnable()
    {
        plugin = this;
        getDataFolder().mkdirs();
        //Scheduler
        scheduler = Executors.newScheduledThreadPool(10);

        //Protocol
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        //ComponentWrapper
        this.componentWrapper = new SimpleComponentWrapper();


        //DataStorage
        accountManager = new AccountManager(scheduler);
        currencyDataStorage = new CurrencyDataStorage(scheduler, 0.1, accountManager);

        //Command
        commandManager = new CommandManager(protocolManager);


        new RankPermissionManager(componentWrapper);

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
