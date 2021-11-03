package net.maxmine.api.common.utility;

import com.google.common.collect.Maps;
import lombok.NonNull;
import net.maxmine.api.common.listener.GameListener;
import org.bukkit.util.ChatPaginator;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@SuppressWarnings("all")
public class StringUtil extends GameListener {

    public static int charCount(String input, char character) {
        return countCharacters(input).get(character);
    }

    private static Map<Character, Integer> countCharacters(String input) {
        Map<Character, Integer> map = Maps.newHashMap();
        for(int i = 0; i < input.length(); i++) {
            map.merge(input.charAt(i), 1, Integer::sum);
        }
        return map;
    }

    public static String getParsedTime(int starttime) {
        int minutes = starttime / 60;
        int seconds = starttime - minutes * 60;

        String m = String.valueOf(minutes);
        String s = String.valueOf(seconds);
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (s.length() == 1) {
            s = "0" + s;
        }
        String color = "";
        return color + m + ":" + s;
    }



    public static String formatingToTime(long ms, String one, String two, String three) {
        if (ms % 100L > 10L && ms % 100L < 15L) {
            return ms + three;
        } else {
            switch ((int) (ms % 10L)) {
                case 1:
                    return ms + one;
                case 2:
                case 3:
                case 4:
                    return ms + two;
                default:
                    return ms + three;
            }
        }
    }

    public static String formatingToTime(long ms, FormatingStringUtils formatingStringUtils) {
        if (ms % 100L > 10L && ms % 100L < 15L) {
            return ms + formatingStringUtils.three;
        } else {
            switch ((int) (ms % 10L)) {
                case 1:
                    return ms + formatingStringUtils.one;
                case 2:
                case 3:
                case 4:
                    return ms + formatingStringUtils.two;
                default:
                    return ms + formatingStringUtils.three;
            }
        }
    }

    public static String getTime(long ms) {
        if (ms < 1000) return "§c0";

        long s = ms / 1000;

        long m = s / 60;
        s %= 60;

        long h = m / 60;
        m %= 60;

        long d = h / 24;
        h %= 24;

        long w = d / 7;
        d %= 7;

        long y = d / 365;
        d %= 365;

        StringBuilder builder = new StringBuilder();

        if (y > 0) builder.append(formatingToTime(y, FormatingStringUtils.YEARS)).append(" ");
        if (w > 0) builder.append(formatingToTime(w, FormatingStringUtils.WEEK)).append(" ");
        if (d > 0) builder.append(formatingToTime(d, FormatingStringUtils.DAYS)).append(" ");
        if (h > 0) builder.append(formatingToTime(h, FormatingStringUtils.HOURS)).append(" ");
        if (m > 0) builder.append(formatingToTime(m, FormatingStringUtils.MINUTES)).append(" ");
        if (s > 0) builder.append(formatingToTime(s, FormatingStringUtils.SECONDS)).append(" ");

        return builder.toString();
    }

    public static String getProgressBar(double curr, double mustHave) {
        double tenPercent = (curr / mustHave) * 30;

        double allPercent = (tenPercent / 30) * 100;

        int percent = (int) Math.round(tenPercent);
        int reached = (int) Math.round(allPercent);

        String progressBar;
        progressBar = org.apache.commons.lang.StringUtils.repeat("§3|", percent);
        progressBar += org.apache.commons.lang.StringUtils.repeat("§8|", (30 - percent));
        progressBar += "     §e(" + reached + "%)";

        return progressBar;
    }

    public static long parseDateDiff(String time, boolean future) throws Exception {
        Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;

        while(m.find()) {
            if(m.group() != null && !m.group().isEmpty()) {
                for(int c = 0; c < m.groupCount(); ++c) {
                    if(m.group(c) != null && !m.group(c).isEmpty()) {
                        found = true;
                        break;
                    }
                }

                if(found) {
                    if(m.group(1) != null && !m.group(1).isEmpty()) {
                        years = Integer.parseInt(m.group(1));
                    }

                    if(m.group(2) != null && !m.group(2).isEmpty()) {
                        months = Integer.parseInt(m.group(2));
                    }

                    if(m.group(3) != null && !m.group(3).isEmpty()) {
                        weeks = Integer.parseInt(m.group(3));
                    }

                    if(m.group(4) != null && !m.group(4).isEmpty()) {
                        days = Integer.parseInt(m.group(4));
                    }

                    if(m.group(5) != null && !m.group(5).isEmpty()) {
                        hours = Integer.parseInt(m.group(5));
                    }

                    if(m.group(6) != null && !m.group(6).isEmpty()) {
                        minutes = Integer.parseInt(m.group(6));
                    }

                    if(m.group(7) != null && !m.group(7).isEmpty()) {
                        seconds = Integer.parseInt(m.group(7));
                    }
                    break;
                }
            }
        }

        if(!found) {
            throw new Exception("Illegal Date");
        } else if(years > 20) {
            throw new Exception("Illegal Date");
        } else {
            GregorianCalendar var13 = new GregorianCalendar();
            if(years > 0) {
                var13.add(1, years * (future?1:-1));
            }

            if(months > 0) {
                var13.add(2, months * (future?1:-1));
            }

            if(weeks > 0) {
                var13.add(3, weeks * (future?1:-1));
            }

            if(days > 0) {
                var13.add(5, days * (future?1:-1));
            }

            if(hours > 0) {
                var13.add(11, hours * (future?1:-1));
            }

            if(minutes > 0) {
                var13.add(12, minutes * (future?1:-1));
            }

            if(seconds > 0) {
                var13.add(13, seconds * (future?1:-1));
            }

            return var13.getTimeInMillis() / 1000L;
        }
    }

    public static String leftTime(long unixTime) {
        long seconds;
        seconds = unixTime - System.currentTimeMillis() / 1000L;


        long minutes = 0L;
        long hours = 0L;
        long days = 0L;
        long i;
        if(seconds >= 60L) {
            i = (long)((int)(seconds / 60L));
            minutes = i;
            seconds %= 60L;
        }

        if(minutes >= 60L) {
            i = (long)((int)(minutes / 60L));
            hours = i;
            minutes %= 60L;
        }

        if(hours >= 24L) {
            i = (long)((int)(hours / 24L));
            days = i;
            hours %= 24L;
        }

        String s = "";
        String msg;
        if(days > 0L) {
            msg = formatTime(days, "д", "ень", "ня", "ней");
            s = s + days + " " + msg + " ";
        }

        if(hours > 0L) {
            msg = formatTime(hours, "час", "", "а", "ов");
            s = s + hours + " " + msg + " ";
        }

        if(minutes > 0L) {
            msg = formatTime(minutes, "минут", "а", "ы", "");
            s = s + minutes + " " + msg + " ";
        }

        if(seconds > 0L) {
            msg = formatTime(seconds, "секунд", "а", "ы", "");
            s = s + seconds + " " + msg + " ";
        }

        return s;
    }

    private static String formatTime(long num, String main, String single, String lessfive, String others) {
        int end = (int)(num % 10L);
        if(num % 100L <= 10L || num % 100L >= 15L) {
            switch(end) {
                case 1:
                    return main + single;
                case 2:
                case 3:
                case 4:
                    return main + lessfive;
            }
        }

        return main + others;
    }


    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public static String centeredMessage(String message) {
        return org.apache.commons.lang.StringUtils.center(message, ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH);
    }

    private static final Pattern SPECIAL_PATTERN_MATCH = Pattern.compile("(@)");

    public static String colorizeString(@NonNull String pattern, @NonNull String... colors) {
        if (colors.length == 0) return SPECIAL_PATTERN_MATCH.matcher(pattern).replaceAll("");

        return applyPattern(pattern, new Function<String, String>() {
            int index = -1;

            @Override
            public String apply(String s) {
                if (index++ >= colors.length) return null;
                return colors[index];
            }
        });

    }

    public static String applyPattern(String pattern, Function<String, String> function) {
        Matcher matcher = SPECIAL_PATTERN_MATCH.matcher(pattern);
        StringBuffer output = new StringBuffer();

        while (matcher.find()) {
            String s = function.apply(matcher.group());
            if (s == null || s.isEmpty()) break;
            matcher.appendReplacement(output, s);
        }
        matcher.appendTail(output);


        return output.toString();
    }


    public static String randomString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String getPattern(@NonNull String string, @NonNull int... positions) {
        StringBuilder sb = new StringBuilder(string);
        for (int position : positions) {
            sb.insert(position + 1, "@");
        }
        return sb.toString();
    }

}
