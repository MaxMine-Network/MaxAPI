package net.maxmine.api.game.listener;

import net.maxmine.api.common.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener extends GameListener {

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

}
