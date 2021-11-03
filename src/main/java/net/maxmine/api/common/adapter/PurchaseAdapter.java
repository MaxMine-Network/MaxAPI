package net.maxmine.api.common.adapter;

import net.maxmine.api.Management;
import net.maxmine.api.game.enums.GameType;
import net.maxmine.api.gamer.Gamer;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by TheMrLiamt
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class PurchaseAdapter extends Adapter {

    public static final String PURCHASE_FIELD = "Purchases";

    private Gamer gamer;
    private List<String> purchases = new ArrayList<>();

    public PurchaseAdapter(Gamer gamer) {
        this.gamer = gamer;
        load();
    }

    public boolean hasItem(String item) {
        return purchases.contains(item);
    }

    public void addItem(GameType type, String item) {
        purchases.add(item);
        Management.getDatabase().execute("INSERT INTO `Stats`.`Purchaces`(`Name`, `Game`, `Item`) VALUES (?, ?, ?)",
                gamer.getName().toLowerCase(),
                type.name(),
                item
        );
    }

    private void load() {
        Management.getDatabase().executeQuery("SELECT * FROM `Stats`.`Purchaces` WHERE `Name` = ?", rs -> {

            while (rs.next())
                purchases.add(rs.getString("Item"));

            return Void.TYPE;
        }, gamer.getName().toLowerCase());
    }

    @Override
    public void save() {

    }

}
