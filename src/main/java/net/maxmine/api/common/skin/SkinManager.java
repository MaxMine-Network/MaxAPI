package net.maxmine.api.common.skin;

import com.google.gson.Gson;
import lombok.Getter;
import net.maxmine.api.Management;
import net.maxmine.api.common.mysql.MySQL;

public class SkinManager {

    private static final MySQL database = Management.getDatabase();

    private static final Gson GSON = new Gson();

    @Getter
    private static SkinManager skinManager;

    public SkinManager() {
        skinManager = this;
    }

    public Skin getSkin(String name) {
        return database.executeQuery("SElECT * FROM `Core`.`SkinData` WHERE `Skin` = ?", (rs) -> {
            if (rs.next())
                return GSON.fromJson(rs.getString("Data"), Skin.class);

            return null;
        }, name.toLowerCase());
    }

    public void cacheSkin(String name, String data) {
        database.execute("INSERT INTO `Core`.`SkinData` (`Skin`, `Data`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Data` = ?", name.toLowerCase(), data, data);
    }

    public String getUUID(String name) {
        return database.executeQuery("SElECT * FROM `Core`.`UUIDData` WHERE `Name` = ?", (rs) -> {
            if (rs.next())
                return rs.getString("Data");

            return null;
        }, name.toLowerCase());
    }

    public void cacheUUID(String name, String uuid) {
        database.execute("INSERT INTO `Core`.`UUIDData` (`Name`, `Data`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Data` = ?", name.toLowerCase(), uuid, uuid);
    }

}
