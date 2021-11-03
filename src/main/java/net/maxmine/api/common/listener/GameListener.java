package net.maxmine.api.common.listener;

import net.maxmine.api.MaxAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class GameListener implements Listener {

    private static final Map<String, Listener> cache = new HashMap<>();
    private static final PluginManager plugin = Bukkit.getPluginManager();

    public GameListener() {
        plugin.registerEvents(this, MaxAPI.getPlugin(MaxAPI.class));

        String name = getClass().getSimpleName();
        cache.put(name, this);

    }

    public static void registerAll(GameListener gameListener) {
        plugin.registerEvents(gameListener, MaxAPI.getPlugin(MaxAPI.class));

        String name = gameListener.getClass().getSimpleName();
        cache.put(name, gameListener);
    }

    public static void registerAll(Class<? extends GameListener> gameListenerClass) {

        try {
            plugin.registerEvents(gameListenerClass.newInstance(), MaxAPI.getPlugin(MaxAPI.class));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterAll(Class<? extends GameListener> gameListenerClass) {

        try {
            HandlerList.unregisterAll(gameListenerClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    public void register() {
        plugin.registerEvents(this, MaxAPI.getInstance());

        String name = this.getClass().getSimpleName();
        cache.put(name, this);
    }

    public static void unregisterAll(GameListener gameListener) {
        String name = gameListener.getClass().getSimpleName();

        HandlerList.unregisterAll(cache.get(name));
    }

    public static void unregisterAll() {
        cache.forEach((name, listener) -> HandlerList.unregisterAll(listener));
    }
}
