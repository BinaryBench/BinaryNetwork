package me.binarynetwork.game.spectate.components;

import com.oracle.jrockit.jfr.EventDefinition;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.game.spectate.SpectateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by Bench on 9/2/2016.
 */
public class DeathSpectate extends ListenerComponent {
    private SpectateManager spectateManager;

    public DeathSpectate(SpectateManager spectateManager)
    {
        this.spectateManager = spectateManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {

        if (!getSpectateManager().getDomainPlayerHolder().test(event.getEntity()))
            return;

        getSpectateManager().enableSpectate(event.getEntity());
    }

    public SpectateManager getSpectateManager()
    {
        return spectateManager;
    }
}
