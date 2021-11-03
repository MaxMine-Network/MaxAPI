package net.maxmine.api.common.tag;

import org.bukkit.entity.Player;

import java.util.Collection;

@SuppressWarnings("all")
public class TagHandler {

    private static TagManager manager;

    public static void init(TagManager manager) {
        TagHandler.manager = manager;
    }

    public static void setTag(Player player, String prefix) {
        setTag(player, prefix);
    }

    public static void setTag(Player player, String prefix, String suffix) {
        setTag(player, player.getName(), prefix, suffix);
    }

    public static void setTag(Player player, String team, String prefix, String suffix) {
        manager.setTag(player, team, prefix, suffix);
    }

    public static void clearTags() {
        manager.clearTags();
    }

    public static void clearTag(Player player) {
        manager.clearTag(player);
    }

    public static boolean hasTag(Player player) {
        return manager.hasTag(player);
    }

    public static TagData getTagData(Player player) {
        return manager.getTagData(player);
    }

    public static Collection<TagData> getDatas() {
        return TagHandler.manager.getDatas();
    }

    public static TagManager getTagManager() {
        return manager;
    }
}
