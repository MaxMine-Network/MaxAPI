package net.maxmine.api.game;

import lombok.NonNull;
import net.maxmine.api.Management;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.common.utility.ItemUtil;
import net.maxmine.api.common.utility.LocationUtil;
import net.maxmine.api.game.board.SpectatorBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("all")
public class SpectatorManager extends GameListener {

    private static final ItemStack lobby = ItemUtil.getBuilder(Material.MAGMA_CREAM).setName("§6Назад в лобби").setLore("", "§7Жми, чтобы вернуться обратно в лобби!").build();

    private static final Set<Player> spectators = new HashSet<>();


    public static Set<Player> getSpectators() {
        return Collections.unmodifiableSet(spectators);
    }

    public static Set<Player> getAlive() {
        final Set<Player> all = new HashSet<>(Bukkit.getOnlinePlayers());
        all.removeAll(spectators);

        return all;
    }

    public static boolean isSpectator(@NonNull Entity entity) {
        return entity instanceof Player && isSpectator((Player) entity);
    }

    public static boolean isSpectator(@NonNull Player player) {
        return spectators.contains(player);
    }

    public static void setSpectator(@NonNull Player player, Player killer, boolean spectator, Location spec) {
        if (spectator) {
            addSpectator(player, killer, spec);
        } else {
            removeSpectator(player);
        }
    }

    public static void addSpectator(@NonNull Player player, Player killer, Location spec) {
        System.out.println("[Spectators] Spectator add: " + player.getName());

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other == player) {
                continue;
            }
            if (spectators.contains(other)) {
                player.showPlayer(other);
                other.showPlayer(player);
            } else {
                other.hidePlayer(player);
            }
        }

        if(killer != null) {
            player.teleport(killer.getLocation());

        } else player.teleport(spec);

        player.setExp(0.0f);
        player.setLevel(0);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setVelocity(new Vector(0, 0, 0));
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
        player.setAllowFlight(true);
        player.setFlying(true);

        Bukkit.getScheduler().runTaskLater(MaxAPI.getInstance(), () -> {
            player.getInventory().setItem(8, lobby);
        },1L);

        SpectatorBoard.sendBord(player);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000000, 0));
        player.spigot().setCollidesWithEntities(false);

        spectators.add(player);

        Management.getGamerLoader().getGamer(player).setSpectator(true);
    }

    public static void removeSpectator(@NonNull Player player) {
        System.out.println("[Spectators] Spectator remove: " + player.getName());

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (player == other) {
                continue;
            }
            if (spectators.contains(other)) {
                player.hidePlayer(other);
                other.hidePlayer(player);
            } else {
                other.showPlayer(player);
            }
        }
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getInventory().clear();
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.spigot().setCollidesWithEntities(true);
        player.setNoDamageTicks(99999);

        spectators.remove(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        spectators.remove(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void spectatorStandBack(PlayerMoveEvent e) {
        Player gamer = e.getPlayer();

        if (isSpectator(gamer)) {
            return;
        }
        Player spectator = LocationUtil.getFirstNearPlayer(gamer.getLocation(), 3.0);

        if (spectator == null || !isSpectator(spectator)) {
            return;
        }

        Vector vector = spectator.getLocation().toVector().subtract(gamer.getLocation().toVector());
        vector.setY(0.8);
        spectator.setVelocity(vector.normalize().multiply(1.1));
    }

    @EventHandler
    public void onSpectatorFall(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (e.getTo().getY() > -10.0) {
            return;
        }
        if (!isSpectator(player)) {
            return;
        }
        Location tp = null;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isSpectator(p)) {
                continue;
            }

            tp = p.getLocation();
            break;
        }
        if (tp == null) {
            tp = e.getTo().clone();
            tp.setY(120.0);
        }
        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(tp);
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (isSpectator(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (isSpectator(e.getDamager())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if (isSpectator(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        if (isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        if (isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent e) {
        if (isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickup(PlayerPickupItemEvent e) {
        if (isSpectator(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (isSpectator(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        if (isSpectator(e.getPlayer())) {

            if(e.getItem().getType().equals(Material.MAGMA_CREAM)) {
                Management.getGamerLoader().getGamer(e.getPlayer()).redirect(Game.getInstance().getGameType().getLobby() + (threadLocalRandom.nextInt(3)+1));
            } else e.setCancelled(true);
        }
    }

}