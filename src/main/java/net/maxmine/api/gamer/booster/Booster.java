package net.maxmine.api.gamer.booster;

import lombok.Getter;
import net.maxmine.api.common.utility.FormatingStringUtils;
import net.maxmine.api.common.utility.StringUtil;

import java.util.concurrent.TimeUnit;

@Getter
public class Booster {

    private long duration;
    private int multiplier;

    private long time;

    public Booster(long duration, int multiplier) {
        this.duration = duration;
        this.multiplier = multiplier;
    }

    public String getName() {
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);

        StringBuilder builder = new StringBuilder();

        builder.append("Бустер §ex").append(multiplier).append("§f на §c");

        if(days > 0)
            builder.append(days).append(" ").append(StringUtil.formatingToTime(days, FormatingStringUtils.DAYS));

        if(hours > 0)
            builder.append(hours).append(" ").append(StringUtil.formatingToTime(hours, FormatingStringUtils.HOURS));

        if(minutes > 0)
            builder.append(minutes).append(" ").append(StringUtil.formatingToTime(minutes, FormatingStringUtils.MINUTES));

        return builder.toString();
    }
    
    public void activate() {
        time += System.currentTimeMillis();
    }

    public boolean isActive() {
        return time > 0 && time > System.currentTimeMillis();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private long duration;
        private int multiplier;

        public Builder minutes(long minutes) {
            duration += TimeUnit.MINUTES.toMillis(minutes);
            return this;
        }

        public Builder hours(long hours) {
            duration += TimeUnit.HOURS.toMillis(hours);
            return this;
        }

        public Builder days(long days) {
            duration += TimeUnit.DAYS.toMillis(days);
            return this;
        }

        public Builder multiplier(int multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public Booster build() {
            return new Booster(duration, multiplier);
        }

    }
}
