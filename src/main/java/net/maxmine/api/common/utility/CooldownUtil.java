package net.maxmine.api.common.utility;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class CooldownUtil {

    private static Table<String, String, Long> cooldowns = HashBasedTable.create();

    public static void startCooldown(String task, String key, long millis) {
        cooldowns.put(task, key, System.currentTimeMillis() + millis);
    }

    public static boolean isExpired(String task, String key) {
        return System.currentTimeMillis() > cooldowns.get(task, key);
    }

    public static void removeCooldown(String task, String key) {
        cooldowns.remove(task, key);
    }

    public static Builder getBuilder(String task, String key) {
        return new Builder(task, key);
    }

    public static class Builder {
        private String task, key;
        private long time;

        public Builder(String task, String key) {
            this.task = task;
            this.key = key;
        }

        public void addDays(long d) {
            time += TimeUnit.DAYS.toMillis(d);
        }

        public void addHours(long h) {
            time += TimeUnit.HOURS.toMillis(h);
        }

        public void addMinutes(long m) {
            time += TimeUnit.MINUTES.toMillis(m);
        }

        public void addSeconds(long s) {
            time += TimeUnit.SECONDS.toMillis(s);
        }

        public void create() {
            CooldownUtil.startCooldown(task, key, time);
        }
    }
}
