package net.maxmine.api.common.hologram;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.hologram.line.TextLine;
import net.maxmine.api.common.hologram.protocol.PacketManager;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public class Hologram {

    private Plugin plugin = MaxAPI.getInstance();
    private Location loc;
    private boolean showedDefault;
    private VisibilityManager visibilityManager = new VisibilityManager(this);
    private List<HologramLine> lines = Lists.newLinkedList();
    private List<BukkitTask> updaters = Lists.newArrayList();

    public Hologram(Location loc) {
        this.loc = loc;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public List<HologramLine> getLines() {
        return lines;
    }

    public void modifyLine(int index, String text) {
        getLine(index).modify(text);
    }

    public void insertLine(String line) {
        insertLine(new TextLine(line));
    }

    public void insertLine(String line, ItemStack item) {
        TextLine textLine = new TextLine(line);
        textLine.setHead(item);

        insertLine(textLine);
    }

    public void teleport(Location location) {
        this.loc = location;

        int count = 0;
        for (HologramLine line : getLines()) {
            line.setLocation(location.clone().add(0, -(0.25 * count), 0));
            count++;

            getVisibilityManager().fill();
            getVisibilityManager().show();
        }
    }

    public void insertLine(HologramLine line) {
        line.setOwningHologram(this);
        lines.add(line);
    }

    public void removeLine(HologramLine line) {
        Preconditions.checkNotNull(line);
        getVisibilityManager().getCanSee().forEach(player -> PacketManager.remove(line, player));
        lines.remove(line);
    }

    public void removeLine(int index) {
        removeLine(getLine(index));
    }

    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    public Location getLocation() {
        return loc;
    }

    public boolean isShowedDefault() {
        return showedDefault;
    }

    public void setShowedDefault(boolean showedDefault) {
        this.showedDefault = showedDefault;
    }

    public void setLine(String line) {
        this.insertLine(line);
    }

    public void addToLocation(double x, double y, double z) {
        for (HologramLine line : getLines()) {
            line.getLocation().add(x, y, z);
            line.getStand().setLocation(line.getLocation().getX(), line.getLocation().getY(), line.getLocation().getZ(), line.getLocation().getPitch(), line.getLocation().getYaw());
            getVisibilityManager().fill();
            getVisibilityManager().refresh();
        }
    }

    public void spawn() {
        spawn(true);
    }

    public void spawn(boolean show) {
        double d = getLines().size() / 4;
        for (HologramLine line : getLines()) {
            line.create(loc.clone().add(0.0D, d - 1, 0.0D));
            d -= 0.3;
        }
        HologramManager.getHologramManager().registerHologram(plugin, this);
        if (!show) return;
        showedDefault = show;
        getVisibilityManager().fill();
        getVisibilityManager().refresh();
    }

    public void setVelocity(Vector velocity) {
        for (HologramLine line : getLines()) {
            getVisibilityManager().getCanSee().forEach(player -> PacketManager.velocity(line, velocity, player));
        }

    }

    public void remove() {
        getVisibilityManager().hide();
        lines.clear();
        updaters.forEach(BukkitTask::cancel);
        updaters.clear();
        HologramManager.getHologramManager().unregisterHologram(this);
    }

    public void addUpdater(long ticks, HologramUpdater updater) {
        updaters.add(Bukkit.getScheduler().runTaskTimer(MaxAPI.getInstance(), () -> updater.performUpdate(this), 0L, ticks));
    }

    public static Builder getBuilder(Location location) {
        return new Builder(location);
    }

    public static class Builder {

        private Hologram hologram;

        Builder(Location location) {
            hologram = new Hologram(location);
        }

        public Builder personal(Player... player) {
            hologram = new PersonalHologram(hologram.getLocation(), player);
            return this;
        }

        public Builder plugin(Plugin plugin) {
            hologram.setPlugin(plugin);
            return this;
        }

        public Builder lineHead(String line, ItemStack item) {
            hologram.insertLine(line, item);
            return this;
        }

        public Builder line(String line) {
            hologram.insertLine(line);
            return this;
        }

        public Builder line(String... lines) {
            for (String line : lines)
                hologram.insertLine(line);
            return this;
        }

        public Builder line(HologramLine line) {
            hologram.insertLine(line);
            return this;
        }

        public Builder updater(long ticks, HologramUpdater updater) {
            hologram.addUpdater(ticks, updater);
            return this;
        }

        public Hologram build() {
            return build(true);
        }

        public Hologram build(boolean show) {
            if (hologram instanceof PersonalHologram) {
                ((PersonalHologram) hologram).create();
            } else {
                hologram.spawn(show);
            }
            return hologram;
        }
    }
}
