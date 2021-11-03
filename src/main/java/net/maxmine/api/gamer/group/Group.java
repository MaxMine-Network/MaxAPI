package net.maxmine.api.gamer.group;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@AllArgsConstructor
@Getter
public class Group {

    private String name;
    private String prefix;
    private String suffix;
    private int level;

    public boolean isDefault() {
        return level == 0;
    }

    public boolean isDonate() {
        return level > 0;
    }
}
