package net.maxmine.api.common.utility;

import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.group.Group;

public class GroupUtil {

    public static String getGroup(Gamer gamer) {
        return getGroup(gamer, gamer.getGroup().getLevel());
    }

    public static String getGroup(Gamer gamer, int level) {
        switch (level) {
            case 10: {
                return "§aVIP";
            }
            case 20: {
                return "§aVIP§6+";
            }
            case 30: {
                return "§bMVP";
            }
            case 40: {
                return "§bMVP§c+";
            }
            case 50: {
                return "§2Хелпер";//Management.getLang().getString(user, "GROUP_HELPER");
            }
            case 60: {
                return "§9Модератор";//Management.getLang().getString(user, "GROUP_MODER");
            }
            case 100: {
                return "§cАдминистратор";//Management.getLang().getString(user, "GROUP_ADMIN");
            }
        }
        return "";//Management.getLang().getString(user, "GROUP_PLAYER");
    }

    public static String getSort(Group group) {
        switch (group.getLevel()) {
            case 100: return "A" + group.getName();
            case 90: return "B" + group.getName();
            case 80: return "C" + group.getName();
            case 50: return "D" + group.getName();
            case 40: return "E" + group.getName();
            case 30: return "F" + group.getName();
            case 20: return "G" + group.getName();
            case 10: return "H" + group.getName();
            default: return "Z" + group.getName();
        }
    }
}
