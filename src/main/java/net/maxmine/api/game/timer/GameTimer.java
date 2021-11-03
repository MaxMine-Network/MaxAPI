package net.maxmine.api.game.timer;

import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.utility.StringUtil;
import net.maxmine.api.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class GameTimer {

    private static BukkitTask task;

    @Getter
    @Setter
    private static int time = 60;

    public static boolean isStarted() {
        return task != null;
    }

    public static void start(Runnable callback) {
        if (task != null) return;

        task = Bukkit.getScheduler().runTaskTimer(MaxAPI.getInstance(), () -> {

            if (time <= 0) {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setExp(0);
                    player.setLevel(0);
                }

                callback.run();
                task.cancel();
                task = null;
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (time % 10 == 0 || time == 15 || time <= 5) {
                    String message = Game.getInstance().getGameType().getPrefix() + "§fИгра начнётся через §c" + StringUtil.formatingToTime(time, " §fсекунду"," §fсекунды"," §fсекунд");

                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, time, time);
                }

                player.setLevel(time);
                player.setExp((float) time / 60);
            }

            time--;


        }, 0L, 20L);
    }
}
