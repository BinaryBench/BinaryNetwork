package me.binarynetwork.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.binarynetwork.core.account.AccountManager;
import me.binarynetwork.core.command.CommandManager;
import me.binarynetwork.core.command.DummyCommand;
import me.binarynetwork.core.command.SimpleCommandWrapper;
import me.binarynetwork.core.common.utils.WorldUtil;
import me.binarynetwork.core.component.SimpleComponentWrapper;
import me.binarynetwork.core.component.components.NoDamage;
import me.binarynetwork.core.currency.CurrencyDataStorage;
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
        scheduler = Executors.newScheduledThreadPool(10);

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        accountManager = new AccountManager(scheduler);
        currencyDataStorage = new CurrencyDataStorage(scheduler, 0.1, accountManager);

        commandManager = new CommandManager(protocolManager);

        commandManager.addCommand(new DummyCommand(), "dummy")
                .addCommand(new SimpleCommandWrapper()
                        .addCommand(new DummyCommand(false, true), "youCantSeeThis")
                        .addCommand(new DummyCommand(), "youCanSeeThis")
                        .addCommand(new DummyCommand(true, false, "This is usage text!", Arrays.asList("This", "is", "usage", "text")), "special"),

                "wrapper");


        WorldUtil.purgeTemporaryWorlds();

        SimpleComponentWrapper simpleComponentWrapper = new SimpleComponentWrapper();
        simpleComponentWrapper.addComponent(new NoDamage(null));

        enable();
    }

    @EventHandler
    public void command(PlayerCommandPreprocessEvent event)
    {

        String[] deferral = event.getMessage().replace("  ", " ").split(" ");
        String[] args = (String[]) ArrayUtils.subarray(deferral, 1, deferral.length);
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        return super.onCommand(sender, command, label, args);
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

    public CommandManager getCommandManager()
    {
        return commandManager;
    }
}
