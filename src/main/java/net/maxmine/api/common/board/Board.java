package net.maxmine.api.common.board;

import net.maxmine.api.common.board.line.BoardLine;
import net.maxmine.api.common.board.updater.BoardUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class Board {

    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective objective = scoreboard.registerNewObjective("board", "dummy");

    private final Updater updater = new Updater();

    private final Map<Integer, BoardLine> LINES = new HashMap<>();
    private final Map<Long, BoardUpdater> UPDATERS = new HashMap<>();

    public Board() {
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void show(Player player) {
        player.setScoreboard(this.scoreboard);

        if (!UPDATERS.isEmpty()) updater.start();
    }

    public void showCommonBoard(Player player) {
        player.setScoreboard(this.scoreboard);

    }

    public void startCommonUpdater() {
        updater.start();
    }

    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }

    public void addLine(int index, String text) {
        BoardLine boardLine = LINES.get(index);

        if (boardLine != null) {
            boardLine.setText(text);
        } else LINES.put(index, new BoardLine(this, index, text));
    }

    public void addLine(String text) {

    }

    public void removeLine(int index) {
        LINES.remove(index).hide();
    }

    public void addUpdater(long time, BoardUpdater boardUpdater) {
        UPDATERS.put(time, boardUpdater);
    }

    public void removeUpdater(Runnable runnable) {
        UPDATERS.clear();
        updater.stop();
    }

    public void remove(Player player) {
        player.setScoreboard(null);
    }


    public void rejectBoard() {
        LINES.values().forEach(BoardLine::hide);
        LINES.clear();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public class Updater extends Thread {

        private final AtomicLong time = new AtomicLong();

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException ignored) {
                }

                if (time.incrementAndGet() == Long.MAX_VALUE) time.set(0);
                UPDATERS.entrySet()
                        .stream()
                        .filter((entry) -> time.get() % entry.getKey() == 0)
                        .forEach(entry -> {
                            if (entry.getValue() != null)
                                entry.getValue().update(Board.this);
                        });
            }
        }
    }
}
