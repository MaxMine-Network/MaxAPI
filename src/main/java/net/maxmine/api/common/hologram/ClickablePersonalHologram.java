package net.maxmine.api.common.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public abstract class ClickablePersonalHologram extends Hologram {

    private Player[] players;

    public ClickablePersonalHologram(Location loc, Player... players) {
        super(loc);
        this.players = players;
    }

    public void create() {
        spawn(false);
        for (Player player : players) {
            getVisibilityManager().show(player);
        }

        HologramManager.getHologramManager().addHologram(this);
    }

    public abstract void onClick(Player player);

}
