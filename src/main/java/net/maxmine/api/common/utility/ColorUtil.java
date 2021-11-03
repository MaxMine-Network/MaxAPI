package net.maxmine.api.common.utility;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class ColorUtil {

    private static final String RAINBOW = "&z";
    private static final String[][] COLOR_CODES = new String[][]{{"&0", "\u00A70"}, {"&1", "\u00A71"},
            {"&2", "\u00A72"}, {"&3", "\u00A73"}, {"&4", "\u00A74"}, {"&5", "\u00A75"}, {"&6", "\u00A76"},
            {"&7", "\u00A77"}, {"&8", "\u00A78"}, {"&9", "\u00A79"}, {"&a", "\u00A7a"}, {"&b", "\u00A7b"},
            {"&c", "\u00A7c"}, {"&d", "\u00A7d"}, {"&e", "\u00A7e"}, {"&f", "\u00A7f"}, {"&k", "\u00A7k"},
            {"&l", "\u00A7l"}, {"&m", "\u00A7m"}, {"&n", "\u00A7n"}, {"&o", "\u00A7o"}, {"&r", "\u00A7r"}};

    /**
     * Replaces &amp;x color to there ChatColor variant. <br>
     * A wrapper for {@link ColorUtil#replaceColors(String)} combines with
     * {@link String#format(String, Object...)}
     *
     * @see ColorUtil#replaceColors(String)
     *
     * @param toReplace
     *            String with &amp;-foramated ChatColor
     * @param args
     *            Works same as the String#format(String, Object...) method.
     *            <br>
     *            for extra help see <a href=
     *            "https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#syntax">
     *            conversion</a>.
     * @return String with ChatColors to there associated position
     */
    public static String replaceColors(String toReplace, Object... args) {
        return replaceColors(String.format(toReplace, args));
    }

    public static String replaceColorsSave(String toReplace, Object... args) {
        return String.format(replaceColors(toReplace), args);
    }

    public static String toRainbow(String input) {
        return rainbow(RAINBOW + input);
    }

    /**
     * Replaces &amp;x color to there ChatColor variant. <br>
     * &amp;0 - BLACK <br>
     * &amp;1 - DARK_BLUE <br>
     * &amp;2 - DARK_GREEN <br>
     * &amp;3 - DARK_AQUA <br>
     * &amp;4 - DARK_RED <br>
     * &amp;5 - DARK_PURPLE <br>
     * &amp;6 - GOLD <br>
     * &amp;7 - GRAY <br>
     * &amp;8 - DARK_GRAY <br>
     * &amp;9 - BLUE <br>
     * &amp;a - GREEN <br>
     * &amp;b - AQUA <br>
     * &amp;c - RED <br>
     * &amp;d - LIGHT_PURPLE <br>
     * &amp;e - YELLOW <br>
     * &amp;f - WHITE <br>
     * &amp;k - MAGIC <br>
     * &amp;l - BOLD <br>
     * &amp;m - STRIKETHROUGH <br>
     * &amp;n - UNDERLINE <br>
     * &amp;o - ITALIC <br>
     * &amp;r - RESET <br>
     * &amp;z - RAINBOW <br>
     *
     * @param toReplace
     *            String with &amp;-foramated ChatColor
     * @return String with ChatColors to there associated position
     */
    public static String replaceColors(String toReplace) {
        if (toReplace == null) {
            return "";
        }
        toReplace = rainbow(toReplace);
        for (String[] strs : COLOR_CODES) {
            toReplace = toReplace.replaceAll(strs[0], strs[1]);
        }
        return toReplace;
    }

    private static String rainbow(String toReplace) {
        if (toReplace.contains(RAINBOW)) {
            String ret = "";
            while (toReplace.contains(RAINBOW)) {
                int start = toReplace.indexOf(RAINBOW);
                int end = start + getNextPos(toReplace.substring(start));
                if (start > 0) {
                    ret += toReplace.substring(0, start);
                }
                ret += doRainbow(toReplace.substring(start, end));
                toReplace = toReplace.substring(end);
            }
            return ret + toReplace;
        }
        return toReplace;
    }

    private static int getNextPos(String str) {
        str = str.replaceFirst(RAINBOW, "");
        int end = str.length();
        for (String[] strs : COLOR_CODES) {
            if (str.contains(strs[0])) {
                end = Math.min(end, str.indexOf(strs[0]));
            }
        }
        if (str.contains(RAINBOW)) {
            end = Math.min(end, str.indexOf(RAINBOW));
        }
        return end + RAINBOW.length();
    }

    private static String doRainbow(String toRainbow) {
        toRainbow = toRainbow.replace(RAINBOW, "");
        String ret = "";
        int i = 10;
        for (char c : toRainbow.toCharArray()) {
            ret += COLOR_CODES[i++][1] + c;
            if (i == 15) {
                i = 10;
            }
        }
        return ret;
    }

    private static Pattern COLOR_MATCH = Pattern.compile("(?i)" + String.valueOf('\u00A7') + "[0-9A-FK-OR]");

    public static String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String insertAll(String text, ChatColor replacement) {
        int size = StringUtil.charCount(text, ChatColor.COLOR_CHAR);
        ChatColor[] replace = new ChatColor[size];
        for(int i = 0; i < replace.length; i++) replace[i] = replacement;
        return insert(text, replace);
    }

    private static String insert(String text, ChatColor... replacement) {
        Matcher matcher = COLOR_MATCH.matcher(text);
        StringBuffer output = new StringBuffer();
        int counter = 0;
        while (matcher.find() && counter < replacement.length) {
            String s = matcher.group();
            ChatColor replace = replacement[counter++];
            if(replace == null) continue;
            matcher.appendReplacement(output, s.concat(replace.toString()));
        }
        matcher.appendTail(output);

        return output.toString();
    }
}
