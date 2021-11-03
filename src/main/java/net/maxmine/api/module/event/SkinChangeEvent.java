package net.maxmine.api.module.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.maxmine.api.common.skin.Skin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class SkinChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Skin skin;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
