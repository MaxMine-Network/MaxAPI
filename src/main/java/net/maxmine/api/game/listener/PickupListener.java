package net.maxmine.api.game.listener;

import net.maxmine.api.common.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupListener extends GameListener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        event.setCancelled(true);
    }

}
