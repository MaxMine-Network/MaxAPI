package net.maxmine.api.common.listener.game;

import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class LobbyListener extends GameListener {

    /**
     * Авторизация игрока.
     * @param event - эвент.
     */

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.teleport(Game.getInstance().getLobbyLocation());
        event.setJoinMessage(null);
    }

    /**
     * Выход игрока с сервера.
     * @param event - эвент.
     */

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    /**
     * Ломание блока игроком.
     * @param event - эвент.
     */

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (playerInSpawn(player)) event.setCancelled(true);
    }

    /**
     * Выставление блока игроком.
     * @param event эвент.
     */

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (playerInSpawn(player)) event.setCancelled(true);
    }


    /**
     * Дейтсвие игрока с предметами
     * @param event - эвент.
     */

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (playerInSpawn(player)) event.setCancelled(true);
    }

    /**
     * Опадание листвы
     * @param event - эвент.
     */

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (spawn()) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnire(BlockIgniteEvent event) {
        if (spawn()) event.setCancelled(true);
    }

    /**
     * Проверка на спавне ли игрок.
     * @param player - игрок.
     * @return - boolean
     */

    private boolean playerInSpawn(Player player) {
        Location location = player.getLocation();
        String worldName = location.getWorld().getName();

        return worldName.equalsIgnoreCase("Lobby");
    }

    /**
     * В каком мире происходят действия.
     * @return - boolean
     */

    private boolean spawn() {
        String serverName = Bukkit.getServer().getName();

        return serverName.equalsIgnoreCase("Lobby");
    }

}
