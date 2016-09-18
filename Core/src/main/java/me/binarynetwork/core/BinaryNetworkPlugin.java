package me.binarynetwork.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandManager;
import me.binarynetwork.core.commands.GameModeCommand;
import me.binarynetwork.core.commands.StopCommand;
import me.binarynetwork.core.commands.TeleportCommand;
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
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
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
        commandManager.addCommand(new StopCommand(permissionWrapper), "stop");
        commandManager.addCommand(new TeleportCommand(permissionWrapper), "teleport", "tp");
        commandManager.addCommand(new ServerCommand(permissionManager, "hub"), "hub", "leave");
        commandManager.addCommand(new GameModeCommand(permissionWrapper, GameMode.CREATIVE), "Creative", "gmc");
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

    public PlayerHolder getServerPlayerHolder()
    {
        return serverPlayerHolder;
    }
}
