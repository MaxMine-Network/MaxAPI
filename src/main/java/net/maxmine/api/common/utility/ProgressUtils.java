package net.maxmine.api.common.utility;

import org.apache.commons.lang.StringUtils;

public class ProgressUtils {

    private String yes, no;
    private int count;
    private double current, max;

    public ProgressUtils(double current, double max, int symbolCount) {
        this.current = current;
        this.max = max;
        this.count = symbolCount;
        this.yes = "§a■";
        this.no = "§7■";
    }

    public ProgressUtils(double current, double max, int symbolCount, String yes, String no) {
        this.current = current;
        this.max = max;
        this.count = symbolCount;
        this.yes = yes;
        this.no = no;
    }

    public String getProgressBar() {
        double tenPercent = (current / max) * count;

        int percent = (int) Math.round(tenPercent);

        String bar = StringUtils.repeat(yes, percent);
        bar += StringUtils.repeat(no, (count - percent));
        return bar;
    }

    public double getPercent() {
        double allPercent = (current / max) * 100;
        return (int) Math.round(allPercent);
    }
}
