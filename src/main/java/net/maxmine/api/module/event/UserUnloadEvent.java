package net.maxmine.api.module.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.maxmine.api.gamer.Gamer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class UserUnloadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Gamer gamer;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}