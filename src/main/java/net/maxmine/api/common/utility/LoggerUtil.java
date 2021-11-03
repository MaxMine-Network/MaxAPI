package net.maxmine.api.common.utility;

import org.bukkit.Bukkit;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class LoggerUtil {

    public static void info(String info, Object... args) {
        Bukkit.getLogger().info(String.format(info, args));
    }
}
