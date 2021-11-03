package net.maxmine.api.common.utility;

import net.maxmine.api.MaxAPI;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@SuppressWarnings("all")
public class TimerUtil {

    private static Map<String, BukkitTask> tasks = new HashMap<>();

    public static void startTask(Runnable r) {
        Bukkit.getScheduler().runTask(MaxAPI.getInstance(), r);
    }

    public static void startTask(Runnable r, long l) {
        Bukkit.getScheduler().runTaskLater(MaxAPI.getInstance(), r, l);
    }

    public static void startAsyncTask(Runnable r, long l) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(MaxAPI.getInstance(), r, l);
    }

    public static void startTimerTask(String name, Runnable r, long l) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(MaxAPI.getInstance(), r, 1L, l);
        tasks.put(name, task);
    }

    public static void startTimerTask(String name, Runnable r, long l, long l2) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(MaxAPI.getInstance(), r, l, l2);
        tasks.put(name, task);
    }

    public static void startTimerTaskAsync(String name, Runnable r, long l) {
        startTimerTaskAsync(name, r, 1L, l);
    }

    public static void startTimerTaskAsync(String name, Runnable r, long l, long l2) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(MaxAPI.getInstance(), r, l, l2);
        tasks.put(name, task);
    }

    public static void stopTask(String name) {
        if(!hasTask(name)) return;
        tasks.get(name).cancel();
        tasks.remove(name);
    }

    public static boolean hasTask(String name) {
        return tasks.containsKey(name);
    }

}
