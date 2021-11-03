package net.maxmine.api.common.hologram;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.common.hologram.protocol.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public class VisibilityManager {
    private Hologram hologram;

    @Getter
    @Setter
    private double visibilityDistance = 64*64;

    private Set<Player> hiddenInRadius = Sets.newConcurrentHashSet();
    private Set<Player> canSee = Sets.newConcurrentHashSet();

    public VisibilityManager(Hologram hologram) {
        this.hologram = hologram;
    }

    public void fill() {
        canSee.clear();
        canSee.addAll(Bukkit.getOnlinePlayers());
    }

    public void refresh() {
        getCanSee().forEach(this::show);
    }

    public void hide() {
        canSee.forEach(this::hide);
    }

    public void show() { canSee.forEach(this::show); }

    public void show(Player player) {
        hologram.getLines().forEach(line -> PacketManager.spawn(line, player));
        canSee.add(player);
    }

    public void hide(Player player) {
        hologram.getLines().forEach(line -> PacketManager.remove(line, player));
        canSee.remove(player);
    }

    public void hideRadius(Player player) {
        if(!canSee(player)) return;
        hologram.getLines().forEach(line -> PacketManager.remove(line, player));
        hiddenInRadius.add(player);
    }

    public void showRadius(Player player) {
        if(!isHiddenInRadius(player)) return;
        show(player);
        hiddenInRadius.remove(player);
    }

    public void update() {
        getCanSee().forEach(this::update);
    }

    public void update(Player player) {
        hologram.getLines().forEach(line -> PacketManager.update(line, player));
    }

    public Set<Player> getCanSee() {
        return canSee;
    }

    public boolean canSee(Player player) {
        return canSee.contains(player);
    }

    public boolean isHiddenInRadius(Player player) {
        return hiddenInRadius.contains(player);
    }
}

