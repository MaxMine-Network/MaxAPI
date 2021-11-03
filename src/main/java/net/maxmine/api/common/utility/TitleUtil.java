package net.maxmine.api.common.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class TitleUtil {

    private String up;
    private String down;

    private int fadein;
    private int fadeOut;

    public static TitleUtil getBuilder() {
        return new TitleUtil();
    }

    public TitleUtil setMessage(String up, String down) {
        this.up = up;
        this.down = down;
        return this;
    }

    public TitleUtil setFadeIn(int fadein) {
        this.fadein = fadein;
        return this;
    }

    public TitleUtil setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public void build() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(up, down, fadein, fadeOut, 20);
        }
    }
}
