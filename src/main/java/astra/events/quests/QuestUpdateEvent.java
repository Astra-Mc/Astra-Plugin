package astra.events.quests;

import astra.playerquestsystem.PlayerQuest;
import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class QuestUpdateEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final PlayerQuest playerQuest;

    public QuestUpdateEvent(Player player, PlayerQuest playerQuest) {
        this.player = player;
        this.playerQuest = playerQuest;
    }

    public static HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerQuest getQuest() {
        return playerQuest;
    }
}