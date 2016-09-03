package me.binarynetwork.game.spectate.components;

import me.binarynetwork.core.component.BaseComponent;
import me.binarynetwork.game.spectate.SpectateManager;

/**
 * Created by Bench on 9/2/2016.
 */
public class JoinSpectate extends BaseComponent {
    private SpectateManager spectateManager;

    public JoinSpectate(SpectateManager spectateManager)
    {
        this.spectateManager = spectateManager;
    }

    @Override
    public void onEnable()
    {
        getSpectateManager().setJoinSpectate(true);
    }

    @Override
    public void onDisable()
    {
        getSpectateManager().setJoinSpectate(false);
    }

    public SpectateManager getSpectateManager()
    {
        return spectateManager;
    }
}
