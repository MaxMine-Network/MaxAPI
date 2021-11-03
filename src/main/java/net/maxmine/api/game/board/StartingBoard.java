package net.maxmine.api.game.board;

import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.board.Board;
import net.maxmine.api.common.board.CommonBoard;
import net.maxmine.api.game.Game;
import net.maxmine.api.game.timer.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class StartingBoard {

    private static final String serverName = Bukkit.getServerName();
    private static final String mapName = MaxAPI.getGameWorld().getName();
    private static final Game game = Game.getInstance();

    private static final Board startingBoard = CommonBoard.getCommonBoard().setDisplayName(Game.getInstance().getGameType().getBoardPrefix())
            .addLine(6, "")
            .addLine(5, "Карта: §a" + mapName)
            .addLine(4, "Игроки: ")
            .addLine(3, "")
            .addLine(2, "Старт через: ")
            .addLine(1, "")
            .addLine(0, "Сервер: §c" + serverName).addUpdater(20L, update -> {
                update.addLine(2, "Старт через: §a" + GameTimer.getTime());
                update.addLine(4, "Игроки: §a" + game.getAlivePlayers().size() + "§7/§a" + game.getSlots());
            }).build();

    public static void sendBord(Player player) {
        startingBoard.showCommonBoard(player);
    }
}
