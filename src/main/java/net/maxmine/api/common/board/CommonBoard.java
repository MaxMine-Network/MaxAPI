package net.maxmine.api.common.board;

import net.maxmine.api.common.board.updater.BoardUpdater;

import java.util.HashMap;
import java.util.Map;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class CommonBoard {

    private Board board = null;
    private String displayName;

    private final Map<Integer, String> cache = new HashMap<>();

    public static CommonBoard getCommonBoard() {
        return new CommonBoard();
    }

    public CommonBoard setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    public CommonBoard addLine(int index, String line) {
        cache.put(index, line);

        return this;
    }

    public CommonBoard addUpdater(long time, BoardUpdater boardUpdater) {
        this.board = new Board();
        this.board.addUpdater(time, boardUpdater);

        return this;
    }

    public Board build() {
        cache.forEach((index, line) -> this.board.addLine(index, line));
        this.board.setDisplayName(this.displayName);
        this.board.startCommonUpdater();

        return this.board;
    }
}
