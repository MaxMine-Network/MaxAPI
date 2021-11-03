package net.maxmine.api.common.adapter;

import lombok.Getter;
import net.maxmine.api.Management;

public class EconomyAdapter extends Adapter {

    public static final String ECONOMY_FIELD = "Money";

    private String player;

    @Getter
    private int money;

    public EconomyAdapter(String player) {
        this.player = player;
        this.money = getPlayerBalance(player);
    }

    public void change(int change) {
        money += change;
    }

    private int getPlayerBalance(String player) {
        return Management.getDatabase().executeQuery("SELECT * FROM `Economy`.`Players` WHERE `Name` = ?", rs -> {

            if(rs.next()) {
                return rs.getInt("Money");
            }

            return 0;
        }, player.toLowerCase());
    }

    @Override
    public void save() {
        Management.getDatabase().execute("INSERT INTO `Economy`.`Players`(`Name`, `Money`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Money` = ?",
                player.toLowerCase(),
                money,
                money);
    }
}
