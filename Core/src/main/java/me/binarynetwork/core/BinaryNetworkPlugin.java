package me.binarynetwork.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandManager;
import me.binarynetwork.core.commands.DefaultCommandManager;
import me.binarynetwork.core.commands.commands.BackGroundCommand;
import me.binarynetwork.core.common.Log;
import me.binarynetwork.core.common.scheduler.Scheduler;
import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.currency.CurrencyManager;
import me.binarynetwork.core.permissions.PermissionManagerWrapper;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.ServerPlayerHolder;
import me.binarynetwork.core.portal.commands.ServerCommand;
import me.binarynetwork.core.rank.RankManager;
import me.binarynetwork.core.permissions.RankPermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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

    private PlayerHolder serverPlayerHolder;

    private SimpleComponentWrapper componentWrapper;
    private ProtocolManager protocolManager;
    private ScheduledExecutorService scheduler;
    private AccountManager accountManager;
    private CurrencyManager currencyManager;
    private CommandManager commandManager;
    private RankManager rankManager;
    private RankPermissionManager permissionManager;

    @Override
    public final void onLoad()
    {
        this.load();
    }

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
        this.serverPlayerHolder = new ServerPlayerHolder();


        //DataStorage
        accountManager = new AccountManager(getScheduler());

        //Permission Stuff
        PermissionManagerWrapper permissionWrapper = new PermissionManagerWrapper();
        rankManager = new RankManager(getScheduler(), getAccountManager(), commandManager, permissionWrapper);
        permissionManager = new RankPermissionManager(getRankManager(), getComponentWrapper());
        permissionWrapper.setWrappedPermissionManager(permissionManager);

        currencyManager = new CurrencyManager(getScheduler(), getAccountManager(), getPermissionManager(), getCommandManager());

        //Commands
        new DefaultCommandManager(commandManager, permissionManager);

        getCommandManager().addCommand(new BackGroundCommand(permissionWrapper), "Background", "bg");

        WorldUtil.purgeTemporaryWorlds(getScheduler());
        componentWrapper.enable();
        enable();
        Scheduler.runSync(this::onPostWorld);
    }

    public final void onPostWorld()
    {
        this.postWorld();
    }

    public void postWorld()
    {

    }



    @Override
    public final void onDisable()
    {
        for (Player player : ServerUtil.getOnlinePlayers())
        {
            PlayerUtil.sendToServer(player, "hub");
        }
        disable();
        componentWrapper.disable();
        WorldUtil.purgeTemporaryWorlds(getScheduler());
    }

    public void load()
    {

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

    public PlayerHolder getServerPlayerHolder()
    {
        return serverPlayerHolder;
    }
}
