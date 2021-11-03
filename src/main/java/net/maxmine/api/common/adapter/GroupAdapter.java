package net.maxmine.api.common.adapter;

import net.maxmine.api.Management;
import net.maxmine.api.common.mysql.MySQL;
import net.maxmine.api.gamer.group.Group;
import net.maxmine.api.gamer.Gamer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class GroupAdapter {

    private Group DEFAULT;

    private final MySQL database = Management.getDatabase();
    private final Map<String, Group> groups = new HashMap<>();

    public GroupAdapter() {
        database.executeQuery("SELECT * FROM `Groups`.`Groups`", rs -> {

            while (rs.next()) {
                String group = rs.getString("Name");
                String prefix = rs.getString("Prefix").replace("&", "§");
                String suffix = rs.getString("Suffix").replace("&", "§");

                int level = rs.getInt("Level");

                Group g = new Group(group, prefix, suffix, level);
                groups.put(group, g);
            }

            return Void.TYPE;
        });

        DEFAULT = groups.get("default");
    }

    public Group getGroupByName(String group) {
        return groups.getOrDefault(group, DEFAULT);
    }

    public Group getOfflinePlayerGroup(String player) {
        return database.executeQuery("SELECT * FROM `Groups`.`Users` WHERE `Name` = ?", rs -> {

            if (rs.next()) {
                return groups.getOrDefault(rs.getString("Group"), DEFAULT);
            }

            return DEFAULT;
        }, player.toLowerCase());
    }

    public Group getGroupByLevel(int level) {
        return getGroupsByLevel(level).get(0);
    }

    private List<Group> getGroupsByLevel(int level) {
        return groups.values().stream()
                .filter(group -> group.getLevel() == level)
                .collect(Collectors.toList());
    }

    public int getMultiplier(Gamer gamer) {
        switch (gamer.getGroup().getLevel()) {
            case 0:
                return 1;
            case 10:
            case 20:
                return 2;

            default:
                return 3;
        }
    }

}
