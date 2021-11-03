package net.maxmine.api.game.listener;

import net.maxmine.api.common.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakListener extends GameListener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }

}
