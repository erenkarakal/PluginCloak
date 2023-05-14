package me.eren.plugincloak.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheatDetectEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public CheatDetectEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}


