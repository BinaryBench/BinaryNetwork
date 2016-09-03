package me.binarynetwork.game.victorycondition;

import me.binarynetwork.core.common.utils.ServerUtil;
import me.binarynetwork.core.component.ListenerComponent;
import me.binarynetwork.core.playerholder.PlayerHolder;
import me.binarynetwork.core.playerholder.events.PlayerRemoveEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bench on 9/2/2016.
 */
public class LMSVictoryCondition extends ListenerComponent {

    private List<Player> rank = new ArrayList<>();

    private PlayerHolder playerHolder;
    private int endPlayersAmount;
    private PlayerHolder broadcast;
    private Runnable whenDone;

    public LMSVictoryCondition(PlayerHolder playerHolder, PlayerHolder broadcast, Runnable whenDone)
    {
        this(playerHolder, 1, broadcast, whenDone);
    }

    public LMSVictoryCondition(PlayerHolder playerHolder, int endPlayersAmount, PlayerHolder broadcast, Runnable whenDone)
    {
        this.playerHolder = playerHolder;
        this.endPlayersAmount = endPlayersAmount;
        this.broadcast = broadcast;
        this.whenDone = whenDone;
    }

    public void checkEnd(int playerCount)
    {
        if (playerCount <= getEndPlayersAmount())
            whenDone.run();
    }

    @EventHandler
    public void playerLeave(PlayerRemoveEvent event)
    {
        if (event.getPlayerHolder() != getPlayerHolder())
            return;

        Player player = event.getPlayer();

        rank.remove(player);
        rank.add(event.getPlayer());
        checkEnd(event.getPostPlayers().size());
    }


    public List<Player> getRank()
    {
        return rank;
    }

    public PlayerHolder getPlayerHolder()
    {
        return playerHolder;
    }

    public int getEndPlayersAmount()
    {
        return endPlayersAmount;
    }

    public PlayerHolder getBroadcast()
    {
        return broadcast;
    }

    public void sendWinners()
    {
        int counter = 0;
        int score = 1;


        List<Player> players = new ArrayList<>(getPlayerHolder().getPlayers());
        players.removeAll(getRank());

        ServerUtil.broadcast("----------", getBroadcast());
        ServerUtil.broadcast("", getBroadcast());

        for (Player player : players)
        {
            counter++;
            print(score, player);
            //if (counter >= 3)
                //break;
        }
        if (counter < 3)
        {
            List<Player> reversed = new ArrayList<>(getRank());
            Collections.reverse(reversed);

            for (Player player : reversed)
            {
                counter++;
                score++;
                print(score, player);
                if (counter >= 3)
                    break;
            }
        }

        ServerUtil.broadcast("", getBroadcast());
        ServerUtil.broadcast("----------", getBroadcast());
    }

    private void print(int rank, Player player)
    {
        ServerUtil.broadcast(rank + ". " + ChatColor.YELLOW + player.getName(), getBroadcast());
    }

    @Override
    public void onDisable()
    {
        sendWinners();
    }

}
