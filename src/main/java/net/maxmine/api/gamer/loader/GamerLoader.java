package net.maxmine.api.gamer.loader;

import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.listener.GamerListener;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class GamerLoader {

    private final Map<String, Gamer> gamers = new HashMap<>();

    public GamerLoader() {
        new GamerListener();
    }
    public void loadGamer(String name) {
        gamers.put(name.toLowerCase(), new Gamer(name));
    }

    public final Gamer getGamer(Player player) {
        return getGamer(player.getName());
    }

    public final Gamer getGamer(String name) {
        return gamers.get(name.toLowerCase());
    }

    public void unloadUser(String name) {
        gamers.remove(name.toLowerCase());
    }

    public List<Gamer> getGamers() {
        return new ArrayList<>(gamers.values());
    }
}
