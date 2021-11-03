package net.maxmine.api.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@AllArgsConstructor
public enum GameState {

    WAITING(0),
    STARTING(1),
    GAME(2),
    ENDING(3);

    @Getter
    private int status;
}
