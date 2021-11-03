package net.maxmine.api.game.listener;

import net.maxmine.api.common.listener.GameListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener extends GameListener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        e.setCancelled(true);
    }


}
