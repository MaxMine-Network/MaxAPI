package net.maxmine.api.game.listener;

import net.maxmine.api.common.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener extends GameListener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }


}
