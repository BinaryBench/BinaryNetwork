package me.binarynetwork.game.spectate;

import me.binarynetwork.core.common.utils.PlayerUtil;
import me.binarynetwork.core.playerholder.PlayerHolder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Created by Bench on 9/2/2016.
 */
public class GameModeSpectateComponent extends BaseSpectateComponent {

    public GameModeSpectateComponent(PlayerHolder playerHolder)
    {
        this(playerHolder, false);
    }

    public GameModeSpectateComponent(PlayerHolder playerHolder, boolean joinSpectate)
    {
        super(playerHolder, joinSpectate);
    }

    @Override
    public void onEnableSpectate(Player player)
    {
        PlayerUtil.resetPlayer(player);
        player.setGameMode(GameMode.SPECTATOR);
    }

    @Override
    public void onDisableSpectate(Player player)
    {

    }
}
