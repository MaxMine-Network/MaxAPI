package net.maxmine.api.common.hologram;

import net.maxmine.api.common.listener.GameListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public class HologramListener extends GameListener {

    private HologramManager manager;

    public HologramListener(HologramManager manager) {
        this.manager = manager;

       // new HologramListener();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for(Hologram hologram : manager.getHolograms()) {
            if(hologram.isShowedDefault()) {
                if(canSee(e.getPlayer(), hologram)) {
                    hologram.getVisibilityManager().show(e.getPlayer());
                } else {
                    hologram.getVisibilityManager().getCanSee().add(e.getPlayer());
                    hologram.getVisibilityManager().hideRadius(e.getPlayer());
                }
            } else {
                if(hologram.getVisibilityManager().canSee(e.getPlayer()) && canSee(e.getPlayer(), hologram)) {
                    hologram.getVisibilityManager().show(e.getPlayer());
                } else {
                    hologram.getVisibilityManager().hideRadius(e.getPlayer());
                }
            }
        }
    }

    private List<String> clicks = new ArrayList<>();

  // public HologramListener() {
  //     ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MaxAPI.getInstance(),
  //             ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
  //         @Override
  //         public void onPacketReceiving(PacketEvent event) {
  //             PacketContainer packet = event.getPacket();
  //             Player player = event.getPlayer();

  //             if(clicks.contains(player.getName())) return;

  //             int id = packet.getIntegers().read(0);
  //             if(id < 0) return;

  //             ClickablePersonalHologram clickablePersonalHologram = HologramManager.getHologramManager().getHolohram(id);
  //             if(clickablePersonalHologram == null) return;
  //             clickablePersonalHologram.onClick(player);
  //             clicks.add(player.getName());

  //             TimerUtils.startTask(() -> clicks.remove(player.getName()), 10L);
  //         }
  //     });
  // }


    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        check(e.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        check(e.getPlayer());
    }

    private void check(Player player) {
        manager.getHolograms().stream()
                .filter(hologram -> hologram.getVisibilityManager().canSee(player))
                .forEach(hologram -> {
                    if(hologram.getVisibilityManager().isHiddenInRadius(player)) {
                        if(canSee(player, hologram)) {
                            hologram.getVisibilityManager().showRadius(player);
                        }
                    } else {
                        if(!canSee(player, hologram)) {
                            hologram.getVisibilityManager().hideRadius(player);
                        }
                    }
                });
    }

    private boolean canSee(Player player, Hologram hologram) {
        return player.getWorld() == hologram.getLocation().getWorld()
                && player.getLocation().distanceSquared(hologram.getLocation()) <= hologram.getVisibilityManager().getVisibilityDistance();
    }
}
