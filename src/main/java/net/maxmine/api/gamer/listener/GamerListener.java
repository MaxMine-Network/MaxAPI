package net.maxmine.api.gamer.listener;

import net.maxmine.api.Management;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.common.tag.TagManager;
import net.maxmine.api.common.utility.GroupUtil;
import net.maxmine.api.game.Game;
import net.maxmine.api.game.enums.GameState;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.group.Group;
import net.maxmine.api.module.event.LevelUpEvent;
import net.maxmine.api.module.event.UserUnloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.*;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class GamerListener extends GameListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncJoin(AsyncPlayerPreLoginEvent e) {
        Management.getGamerLoader().loadGamer(e.getName());

        Group group = Management.getGroupAdapter().getOfflinePlayerGroup(e.getName());
        int online = Bukkit.getOnlinePlayers().size();

        if(Game.getInstance() != null) {
            Game game = Game.getInstance();
            if(game.getState() == GameState.GAME) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "§cИгра заполнена");
            } else if(group.getLevel() > 0 && (game.getState() == GameState.WAITING || game.getState() == GameState.STARTING) && game.getSlots() == online) {
                Gamer random = Management.getGamerLoader().getGamers().stream()
                        .filter(gamer -> gamer.getGroup().isDefault())
                        .findAny()
                        .orElse(null);

                if(random != null) {
                    random.getPlayer().kickPlayer("§cКто-то занял Ваше место. Что бы такого не происходило, можете купить любую из доступных привилегий на сайте maxmine.su");
                } else {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "§cИгра заполнена");
                }
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();

        if(e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            Management.getGamerLoader().unloadUser(player.getName());
            return;
        }

        Gamer gamer = Management.getGamerLoader().getGamer(player);

        gamer.setPlayer(player);

        player.setDisplayName(gamer.getGroup().getPrefix() + gamer.getName());

        int groupLevel = gamer.getGroup().getLevel();

        if (groupLevel > 0) {
            player.setAllowFlight(true);

            if(groupLevel >= 100) {
                player.setOp(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);

        Player player = e.getPlayer();
        Gamer gamer = Management.getGamerLoader().getGamer(player);

        TagManager manager = TagManager.getManager();

        manager.sendTags(player);

        if (!manager.updateTag(player, gamer.getGroup().getPrefix(), ""))
            manager.setTag(player, GroupUtil.getSort(gamer.getGroup()), gamer.getGroup().getPrefix(), "");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        Gamer gamer = Management.getGamerLoader().getGamer(player);

        String format = "%name%%suffix%: %message%";
        format = format.replace("%name%", "%1$s");
        format = format.replace("%suffix%", gamer.getGroup().getSuffix());
        format = format.replace("%message%", "%2$s");

        e.setFormat(format);
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent e) {
        Gamer gamer = e.getGamer();

        Player player = gamer.getPlayer();

        player.sendTitle("§6§lУРОВЕНЬ ПОВЫШЕН!", "§6" + (int) e.getOldLevel() + " §f-> §6" + (int) e.getNewLevel());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player player = e.getPlayer();

        Gamer gamer = Management.getGamerLoader().getGamer(player);

        if(gamer == null) return;

        UserUnloadEvent userUnloadEvent = new UserUnloadEvent(gamer);
        Bukkit.getPluginManager().callEvent(userUnloadEvent);

        gamer.save();

        Management.getGamerLoader().unloadUser(e.getPlayer().getName());
    }


}
