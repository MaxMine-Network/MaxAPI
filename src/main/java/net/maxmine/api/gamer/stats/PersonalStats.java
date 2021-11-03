package net.maxmine.api.gamer.stats;

import net.maxmine.api.Management;
import net.maxmine.api.common.adapter.Adapter;
import net.maxmine.api.common.mysql.MySQL;

import java.util.HashMap;
import java.util.Map;

public class PersonalStats extends Adapter {

    public static final String STATS_FIELD = "PersonalStats";

    private static String gameName;
    private String playerName;
    private static final String SELECT_STAT = "SELECT * FROM `GameStats`.`%s` WHERE `name` = ?";
    private static final String INSERT_SQL = "INSERT INTO `GameStats`.`%s`(`name`, `%s`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `%s` = `%s` + ?";

    private static final Map<String, Integer> gameData = new HashMap<>();

    private static final MySQL database = Management.getDatabase();

    public PersonalStats(String playerName) {
        this.playerName = playerName;
    }

    public static void setGameName(String gameName) {
        PersonalStats.gameName = gameName;
    }

    public int getStats(String column) {
        try {
            return database.executeQuery(String.format(SELECT_STAT, PersonalStats.gameName), rs -> {
                if (rs.next()) {
                    return rs.getInt(column);
                } else {
                    return 0;
                }
            }, this.playerName.toLowerCase());
        } catch (Exception ex) {
            return 0;
        }
    }

    public void addTeamStat(String name, int add) {
        gameData.merge(name.toLowerCase(), add, Integer::sum);
    }

    public void addStat(String fieldAccessor, int add) {
        database.execute(String.format(INSERT_SQL, gameName, fieldAccessor, fieldAccessor, fieldAccessor), playerName.toLowerCase(), add, add);
    }

    public static String getGameName() {
        return PersonalStats.gameName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getTempStat(String name) {
        return gameData.getOrDefault(name.toLowerCase(), 0);
    }

    @Override
    public void save() {
        for (Map.Entry<String, Integer> data : gameData.entrySet()) {
            addStat(data.getKey(), data.getValue());
        }
    }
}
