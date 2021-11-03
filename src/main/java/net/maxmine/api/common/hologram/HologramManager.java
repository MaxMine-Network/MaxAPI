package net.maxmine.api.common.hologram;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public class HologramManager {

    private final ConcurrentMap<Plugin, List<Hologram>> holograms = new ConcurrentHashMap<>();

    @Getter
    private static HologramManager hologramManager;

    public HologramManager() {
        hologramManager = this;
        new HologramListener(hologramManager);
    }

    private static final FieldAccessor ENTITY_ID = Accessors.getFieldAccessor(
            MinecraftReflection.getEntityClass(), "entityCount", true);

    private HashMap<Integer, ClickablePersonalHologram> holo = new HashMap<>();

    public void addHologram(ClickablePersonalHologram clickablePersonalHologram) {
        int id = (int) ENTITY_ID.get(null) - 1;
        holo.put(id, clickablePersonalHologram);
    }

    public ClickablePersonalHologram getHolohram(int id) {
        return holo.get(id);
    }

    public Hologram createHologram(Location location) {
        return new Hologram(location);
    }

    public void registerHologram(Plugin plugin, Hologram hologram) {
        getHolograms(plugin).add(hologram);
    }

    public void unregisterHolograms(Plugin plugin) {
        holograms.get(plugin).forEach(Hologram::remove);
        holograms.remove(plugin);
    }

    public void unregisterHologram(Hologram hologram) {
        holograms.entrySet().stream()
                .filter(entry -> entry.getValue() == hologram)
                .forEach(entry -> entry.getValue().forEach(Hologram::remove));
    }

    public List<Hologram> getHolograms() {
        List<Hologram> holograms = Lists.newArrayList();
        for (List<Hologram> list : this.holograms.values()) {
            holograms.addAll(list);
        }
        return holograms;
    }

    public List<Hologram> getHolograms(Plugin plugin) {
        return holograms.computeIfAbsent(plugin, k -> Lists.newArrayList());
    }

    public void removeHolograms() {
        holograms.values().forEach(list -> list.forEach(Hologram::remove));
        holograms.clear();
    }
}

