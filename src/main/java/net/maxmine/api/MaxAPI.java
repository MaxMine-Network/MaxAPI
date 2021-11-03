package net.maxmine.api;

import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.coreclient.connection.CoreConnection;
import net.maxmine.api.coreclient.packets.Core;
import net.maxmine.api.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class MaxAPI extends JavaPlugin {

    private static MaxAPI INSTANCE;

    public static MaxAPI getInstance() {
        return INSTANCE;
    }

    @Setter
    @Getter
    private static World gameWorld;

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();

        INSTANCE = this;

        new Management();
        new Core();

        getLogger().info(String.format("§f[§cMaxAPI§f] Has been loaded in (%d ms)", System.currentTimeMillis() - time));
    }

    @Override
    public void onDisable() {
        CoreConnection.disconnect();
        if(Game.getInstance() != null) {
            Bukkit.unloadWorld(gameWorld, false);
        }

        Core.getConnectionThread().interrupt();
    }
}

