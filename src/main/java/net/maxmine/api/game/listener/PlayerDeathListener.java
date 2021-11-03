package net.maxmine.api.game.listener;

import net.maxmine.api.Management;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.game.Game;
import net.maxmine.api.gamer.Gamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerDeathListener extends GameListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Gamer gamer = Management.getGamerLoader().getGamer(event.getPlayer());

        if (gamer.isSpectator()) return;

        if (event.getTo().getY() < 0) {
            Player player = event.getPlayer();
            Player killer = null;
            Entity entity = null;
            if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                entity = ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager();
            }
            if (entity instanceof Player)
                killer = (Player) entity;

            gamer.setDamageCause(EntityDamageEvent.DamageCause.VOID);

            Game.getInstance().onDeath(player, killer);
            Game.getInstance().checkEnd();

            gamer.setDamageCause(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
            player.setLastDamageCause(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(PlayerDeathEvent event) {
        Gamer gamer = Management.getGamerLoader().getGamer(event.getEntity());

        Player player = event.getEntity();

        event.setDeathMessage(null);

        event.getDrops().clear();
        event.getEntity().getInventory().clear();

        if(player.getLastDamageCause() != null) {
            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                return;
            }

            gamer.setDamageCause(event.getEntity().getLastDamageCause().getCause());
        }

        Game.getInstance().onDeath(event.getEntity(), event.getEntity().getKiller());
        Game.getInstance().checkEnd();

        player.setLastDamageCause(null);
    }

}
