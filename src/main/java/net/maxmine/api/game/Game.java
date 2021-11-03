package net.maxmine.api.game;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.Management;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.common.listener.game.LobbyListener;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.manager.PacketManager;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.coreclient.packets.game.GetKitPacket;
import net.maxmine.api.game.board.StartingBoard;
import net.maxmine.api.game.board.WaitingBoard;
import net.maxmine.api.game.enums.GameSettings;
import net.maxmine.api.game.enums.GameState;
import net.maxmine.api.game.enums.GameType;
import net.maxmine.api.game.selector.GameMapSelector;
import net.maxmine.api.game.timer.GameTimer;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.loader.GamerLoader;
import net.maxmine.api.module.perk.PerkSettings;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.List;

public abstract class Game extends GameExtension {

    @Getter
    private static Game instance;

    /* Game Info */

    @Getter
    private GameType gameType;

    @Getter
    private int slots;

    @Getter
    @Setter
    private Location lobbyLocation;

    /*    END    */

    public Game(GameType gameType, int slots) {
        instance = this;

        this.gameType = gameType;
        this.slots = slots;

        state = GameState.WAITING;
        setStatus(state);

        MaxAPI.setGameWorld(new GameMapSelector().selectMap());

        sendMapPacket();

        registerListener(new SpectatorManager());
    }

    public void sendMapPacket() {
        Packet mapPacket = new Packet() {
            @Override
            public void write(PacketBuffer packetBuffer) throws IOException {
                packetBuffer.writeIntLE(150);
                packetBuffer.writeString(Bukkit.getServerName());
                packetBuffer.writeString(MaxAPI.getGameWorld().getName());
            }

            @Override
            public void handle(PacketBuffer packetBuffer) throws IOException {

            }

            @Override
            public void process(Channel channel) throws IOException {

            }
        };

        PacketManager.sendPacket(mapPacket);
    }

    public abstract void onDeath(Player player, Player killer);

    public abstract void onStart();

    public abstract void onEnd(Player player);

    private void checkStart() {
        if (state == GameState.WAITING && ((Bukkit.getOnlinePlayers().size() >= (slots / 2))))
            start();
        else if (state == GameState.STARTING && Bukkit.getOnlinePlayers().size() >= slots) {
            GameTimer.setTime(10);
            state = GameState.STARTING;
            setStatus(state);
        }
    }

    public void start() {
        if (!settings.getOrDefault(GameSettings.API_STARTGAME, true)) return;

        for (Player player : Bukkit.getOnlinePlayers())
            StartingBoard.sendBord(player);

        if (settings.getOrDefault(GameSettings.API_AWAYSTART, false))
            GameTimer.setTime(0);

        GameTimer.start(() -> {
            state = GameState.GAME;
            setStatus(state);

            gameListeners.forEach(GameListener::register);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);

                player.setAllowFlight(false);

                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
            }

            onStart();
            PerkSettings.gameStart();

            for (Gamer gamer : users.getGamers()) {
                if(gamer.getKit() != null)
                    gamer.getKit().getItemStacks().forEach(item -> gamer.getPlayer().getInventory().addItem(item));
            }
        });
    }

    public void checkEnd() {
        if (!settings.getOrDefault(GameSettings.API_ENDGAME, true)) return;
        if (state == GameState.GAME && getAlivePlayers().size() < 2) {
            if(getAlivePlayers().size() <= 0) {
                Bukkit.shutdown();
                return;
            }
            state = GameState.ENDING;
            setStatus(state);

            gameListeners.forEach(GameListener::unregister);
            registerListener(new LobbyListener());

            fixVisible();
            Player winner = getAlivePlayers().get(0).getPlayer();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);

                player.getInventory().clear();
                player.getInventory().setArmorContents(null);

                player.teleport(lobbyLocation);
            }

            List<Gamer> gamers = Management.getGamerLoader().getGamers();

            for (Gamer gamer : gamers) {
                if (gamer.isSpectator()) {
                    Player player = Bukkit.getPlayer(gamer.getName());
                    SpectatorManager.removeSpectator(player);
                }
            }

            try { onEnd(winner); } catch (Exception ignored) {}

            shutdown();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String message = "%s §fприсоединился к игре (§c%d§f/§c%d§f)";

        Player player = e.getPlayer();
        player.teleport(lobbyLocation);
        player.setGameMode(GameMode.ADVENTURE);

        if (state == GameState.WAITING)
            WaitingBoard.sendBord(player);
        else if (state == GameState.STARTING)
            StartingBoard.sendBord(player);

        Bukkit.broadcastMessage(String.format(getGameType().getPrefix() + message, player.getDisplayName(), Bukkit.getOnlinePlayers().size(), slots));
        checkStart();

        GetKitPacket packet = new GetKitPacket(player.getName(), gameType.name());
        PacketManager.sendPacket(packet);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (state == GameState.GAME) {
            checkEnd();
            return;
        }

        String message = "%s §fпокинул игру (§c%d§f/§c%d§f)";

        Bukkit.broadcastMessage(String.format(message, e.getPlayer().getDisplayName(), Bukkit.getOnlinePlayers().size() - 1, slots));
    }
}
