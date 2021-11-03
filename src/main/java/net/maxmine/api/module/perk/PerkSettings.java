package net.maxmine.api.module.perk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class PerkSettings {

    private static final List<PerkAdapter> perks = new ArrayList<>();

    public static void perkAccept(PerkAdapter perkAdapter) {
        perks.add(perkAdapter);
    }

    public static void perkReject(PerkAdapter perkAdapter) {
        perks.remove(perkAdapter);
    }

    public static void gameStart() {
        for(PerkAdapter perkAdapter : perks) {
            perkAdapter.activateAllPerks();
        }
    }

    public static Collection<PerkAdapter> getPerks() {
        return perks;
    }

}
