package net.maxmine.api.module.perk.gui;

import net.maxmine.api.Management;
import net.maxmine.api.common.adapter.GroupAdapter;
import net.maxmine.api.common.gui.Gui;
import net.maxmine.api.common.utility.ItemUtil;
import net.maxmine.api.game.Game;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.group.Group;
import net.maxmine.api.module.perk.PerkAdapter;
import net.maxmine.api.module.perk.PerkSettings;
import org.bukkit.entity.Player;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class PerkGui extends Gui {

    public PerkGui(Player player) {
        super(player, "Перки", 3);
    }

    private static final GroupAdapter groups = Management.getGroupAdapter();

    @Override
    public void drawInventory() {
        int slot = 11;

        Gamer gamer = Management.getGamerLoader().getGamer(this.getPlayer());

        for(PerkAdapter perkAdapter : PerkSettings.getPerks()) {
            ItemUtil perk = ItemUtil.getBuilder(perkAdapter.getDisplay_item().clone());

            if(perkAdapter.getMin_level() > gamer.getGroup().getLevel()) {
                Group group = groups.getGroupByLevel(perkAdapter.getMin_level());

                perk.addLore(
                        "",
                        "§cВы не можете выбрать данный перк!",
                        "§cДля того, чтобы выбрать, Вам необходимо,",
                        "§7* §cПривилегия: " + group.getPrefix() + "§cУ Вас " + (gamer.getGroup().isDefault() ? "§7Игрок" : gamer.getGroup().getPrefix()));

            } else if(perkAdapter.gamerIsSelectedPerk(gamer)) {
                perk.addLore("","§cУ Вас уже выбран данный перк!");
            } else perk.addLore("","§aЖми, чтобы выбрать перк!");

            perk.setName("§6" + perkAdapter.getPerk_name());

            addItem(slot, perk.build(), p -> {

                if(perkAdapter.getMin_level() > gamer.getGroup().getLevel()) {
                    return;
                } else {
                    if(perkAdapter.gamerIsSelectedPerk(gamer)) {
                        return;
                    }
                }

                perkAdapter.acceptPerk(gamer);
                p.sendMessage(Game.getInstance().getGameType().getPrefix() + "Вы успео выбрали перк '§c" + perkAdapter.getPerk_name() + "§f' Когда игра начнётся он будет активирован!");
            });

            slot++;
        }
    }
}
