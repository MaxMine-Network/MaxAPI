package net.maxmine.api.gamer.level;

import net.maxmine.api.Management;
import net.maxmine.api.common.mysql.MySQL;

public class LevelManager {

    private static final MySQL database = Management.getDatabase();

    private static final String GET_EXP_SQL = "SELECT * FROM `Stats`.`Levels` WHERE `Name` = ?";
    private static final String SAVE_EXP_SQL = "INSERT INTO `Stats`.`Levels`(`Name`, `EXP`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `EXP` = ?";

    public static double loadExp(String name) {
        return database.executeQuery(GET_EXP_SQL, rs -> {
            if(rs.next())
                return rs.getDouble("EXP");

            return 0.0D;
        }, name.toLowerCase());
    }

    public static void saveExp(String name, double exp) {
        database.execute(SAVE_EXP_SQL, name.toLowerCase(), exp, exp);
    }

}
