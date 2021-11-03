package net.maxmine.api.common.utility;

import com.google.common.collect.Maps;
import net.maxmine.api.common.packet.packetwrapper.WrapperPlayServerScoreboardTeam;
import net.maxmine.api.common.tag.TagData;
import net.maxmine.api.common.tag.TagHandler;
import net.maxmine.api.common.tag.TagPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("all")
public class TagUtil {

    public static String fix(String input, int maxLength) {
        if(input.startsWith("§f")) input = input.replaceFirst("§f", "");
        if(input.length() > maxLength) return input.substring(0, maxLength);
        return input;
    }

    public static String fix(String input) {
        return fix(input, 16);
    }

    public static String fixTeam(String input) {
        return input.length() > 14 ? input.substring(0, 14) : input;
    }

    private static Map<Integer, String> sorts = Maps.newHashMap();
    private static Map<Player, String> prefixes = Maps.newHashMap();

    public static void greenName(Player player) {
        greenNames(Collections.singletonList(player));
    }

    public static void greenTeam(Map<String, String> teams) {
        teams.forEach((p1, p2) -> {

            Player player1 = Bukkit.getPlayer(p1);
            Player player2 = Bukkit.getPlayer(p2);

            if(player1 != null) TagHandler.getTagManager().getDatas().forEach(data -> data.destroy(player1));
            if(player2 != null) TagHandler.getTagManager().getDatas().forEach(data -> data.destroy(player2));

            for (Player p : Bukkit.getOnlinePlayers()) {
                TagPacket redPacket = new TagPacket("B" + p.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, p);
                redPacket.insertData(new TagData(redPacket.getName(), "§c" + getPrefix(p), ""));
                if(player1 != null) redPacket.send(player1);
                if(player2 != null) redPacket.send(player2);
            }

            if(player1 != null) {
                TagPacket packet = new TagPacket("A" + player1.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, player1);
                packet.insertData(new TagData(packet.getName(), "§a" + getPrefix(player1), ""));
                packet.send(player1);
                if(player2 != null) packet.send(player2);
            }

            if(player2 != null) {
                TagPacket packet = new TagPacket("A" + player2.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, player2);
                packet.insertData(new TagData(packet.getName(), "§a" + getPrefix(player2), ""));
                packet.send(player2);
                if(player1 != null) packet.send(player1);
            }

        });
    }

    public static void greenNames(Collection<Player> players) {
        players.forEach(player -> {
            TagHandler.getTagManager().getDatas().forEach(data -> data.destroy(player));

            for(Player p : Bukkit.getOnlinePlayers()) {
                TagPacket redPacket = new TagPacket("B" + p.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, p);
                redPacket.insertData(new TagData(redPacket.getName(), "§c" + getPrefix(p), ""));
                redPacket.send(player);
            }

            TagPacket packet = new TagPacket("A" + player.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, player);
            packet.insertData(new TagData(packet.getName(), "§a" + getPrefix(player), ""));
            packet.send(player);
        });
    }

    public static void greenTeams(Collection<Player> players) {
        for(Player player : players) {
            TagHandler.getTagManager().getDatas().forEach(data -> data.destroy(player));

            for(Player p: Bukkit.getOnlinePlayers()) {
                if(players.contains(p)) continue;
                TagPacket redPacket = new TagPacket("B" + p.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, p);
                redPacket.insertData(new TagData(redPacket.getName(), "§c" + getPrefix(p), ""));
                redPacket.send(player);
            }

            TagPacket packet = new TagPacket("A" + player.getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED, player);
            packet.insertData(new TagData(packet.getName(), "§a" + getPrefix(player), ""));
            players.forEach(packet::send);
        }
    }

    public static void setPrefix(Player player, String prefix) {
        prefixes.put(player, prefix);
    }

    private static String getPrefix(Player player) {
        if(!prefixes.containsKey(player)) return "";
        return prefixes.get(player);
    }


    public static String getSort(int groupLevel) {
        if(!sorts.containsKey(groupLevel)) {
            String sort = groupLevel > 25 ? "A" : String.valueOf((char)('Y' - groupLevel)).toUpperCase();
            sorts.put(groupLevel, sort);
        }
        return sorts.get(groupLevel);
    }
}