package net.maxmine.api.module.perk;

import lombok.Getter;
import net.maxmine.api.gamer.Gamer;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@Getter
public abstract class PerkAdapter implements WorkFactory {

    private final List<Gamer> gamersPerk = new ArrayList<>();
    private static final Map<String, PerkAdapter> selected_perk = new HashMap<>();

    private String perk_name;
    private int min_level;
    private ItemStack display_item;

    public PerkAdapter() {
        PerkSettings.perkAccept(this);
    }

    protected void perkInGame(Gamer gamer) {

    }

    public void setName(String perk_name) {
        this.perk_name = perk_name;
    }

    public void setLevel(int min_level) {
        this.min_level = min_level;
    }

    public void setDisplayItem(ItemStack display_item) {
        this.display_item = display_item;
    }

    public void acceptPerk(Gamer gamer) {
        if(selected_perk.containsKey(gamer.getName().toLowerCase())) selected_perk.get(gamer.getName().toLowerCase()).rejectPerk(gamer);

        gamersPerk.add(gamer);
    }

    public void rejectPerk(Gamer gamer) {
        gamersPerk.remove(gamer);
    }

    public boolean gamerIsSelectedPerk(Gamer gamer) {
        return gamersPerk.contains(gamer);
    }

    public Collection<Gamer> getGamers() {
        return gamersPerk;
    }

    public void activateAllPerks() {
        for (Gamer gamer : gamersPerk) {
            perkInGame(gamer);
        }
    }
}
