package net.maxmine.api.game;

import io.netty.channel.Channel;
import lombok.Getter;
import net.maxmine.api.Management;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.common.listener.game.LobbyListener;
import net.maxmine.api.common.utility.TimerUtil;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.manager.PacketManager;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.coreclient.packets.party.GetPartyPacket;
import net.maxmine.api.coreclient.packets.player.PlayerRedirectPacket;
import net.maxmine.api.game.enums.GameSettings;
import net.maxmine.api.game.enums.GameState;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.loader.GamerLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Getter
public class GameExtension extends GameListener {

    public ThreadLocalRandom random = ThreadLocalRandom.current();

    public List<GameListener> gameListeners = new ArrayList<>();
    public Map<GameSettings, Boolean> settings = new HashMap<>();

    public GamerLoader users = Management.getGamerLoader();

    public GameState state;

    public boolean god;

    /**
     * Set god mode for all players
     * @param time
     */
    public void setGodMode(int time) {
        god = true;
        TimerUtil.startTask(() -> god = false, 20L * time);
    }

    /**
     * Register listener on game
     * @param listener
     */
    public void registerListener(GameListener listener) {
        listener.register();
        gameListeners.add(listener);
    }

    /**
     * Broadcast message without game prefix
     * @param message
     */
    public void message(String message) {
        message(message, false);
    }

    /**
     * Broadcast message
     * @param message
     * @param needPrefix
     */
    public void message(String message, boolean needPrefix) {
        Bukkit.broadcastMessage((needPrefix ? Game.getInstance().getGameType().getPrefix() : "") + message);
    }

    /**
     * Set game setting
     * @param setting
     * @param value
     */
    public void setSetting(GameSettings setting, Boolean value) {
        settings.put(setting, value);
    }

    /**
     * Get all alive players
     * @return user list
     */
    public List<Gamer> getAlivePlayers() {
        return users.getGamers().stream()
                .filter(user -> !user.isSpectator())
                .collect(Collectors.toList());
    }

    /**
     * Fix players visible
     */
    public void fixVisible() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Gamer gamer : getAlivePlayers()) {
                player.showPlayer(gamer.getPlayer());
                gamer.getPlayer().showPlayer(player);
            }
        }
    }

    /**
     * Send status packet to core
     * @param state
     */
    public void setStatus(GameState state) {
        Packet statusPacket = new Packet() {
            @Override
            public void write(PacketBuffer packetBuffer) throws IOException {
                packetBuffer.writeIntLE(152);
                packetBuffer.writeString(Bukkit.getServerName());
                packetBuffer.writeIntLE(state.getStatus());
            }

            @Override
            public void handle(PacketBuffer packetBuffer) throws IOException {

            }

            @Override
            public void process(Channel channel) throws IOException {

            }
        };

        PacketManager.sendPacket(statusPacket);
    }

    public void shutdown() {
        message("§cПерезагрузка сервера через 15 секунд...");

        TimerUtil.startTask(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String name = player.getName();

                PlayerRedirectPacket packet = new PlayerRedirectPacket(name, Game.getInstance().getGameType().getLobby() + (random.nextInt(2) + 1));
                PacketManager.sendPacket(packet);
            }

            Bukkit.unloadWorld(MaxAPI.getGameWorld(), false);
        }, 20L * 13L);

        TimerUtil.startTask(Bukkit::shutdown, 20L * 15L);
    }

    @EventHandler
    public void onGodCheck(EntityDamageEvent e) {
        if(god)
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncJoin(AsyncPlayerPreLoginEvent e) {
        if(e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.KICK_FULL) {
            GetPartyPacket packet = new GetPartyPacket(e.getName());

            TimerUtil.startTask(() -> PacketManager.sendPacket(packet), 10L);
        }
    }
}
