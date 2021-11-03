package net.maxmine.api.common.hologram;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public interface HologramLine {

    void setOwningHologram(Hologram hologram);

    Hologram getOwningHologram();

    void create(Location loc);

    void modify(String text);

    void setLocation(Location location);

    EntityArmorStand getStand();

    Location getLocation();
}
