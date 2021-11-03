package net.maxmine.api.common.adapter;

import lombok.Getter;
import net.maxmine.api.Management;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.booster.Booster;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("all")
public class BoosterAdapter extends Adapter {

    public static final String BOOSTER_FIELD = "Boosters";

    private static final String GET_BOOSTERS = "SELECT * FROM `Stats`.`Boosters` WHERE `Name` = ?";
    private static final String SET_BOOSTERS = "INSERT INTO `Stats`.`Boosters`(`Name`, `Data`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Data` = ?";

    private final String name;

    private Booster activeBooster;
    private List<Booster> boosters = new ArrayList<>();

    public BoosterAdapter(String name) {
        this.name = name;

        Management.getDatabase().executeQuery(GET_BOOSTERS, rs -> {

            if (rs.next()) {
                List<Booster> bs = Management.GSON.fromJson(rs.getString("Data"), List.class);

                for (Booster booster : bs) {
                    if (booster.isActive()) {
                        activeBooster = booster;
                    }

                    boosters.add(booster);
                }
            }

            return Void.TYPE;
        }, name.toLowerCase());
    }

    public void giveBooster(Booster booster) {
        Gamer gamer = Management.getGamerLoader().getGamer(name);

        boosters.add(booster);

        if(gamer != null)
            gamer.sendActionBar("§fВы получили " + booster.getName().toLowerCase());
    }

    @Override
    public void save() {
        String json = Management.GSON.toJson(boosters);

        Management.getDatabase().execute(SET_BOOSTERS, name.toLowerCase(), json, json);
    }
}
