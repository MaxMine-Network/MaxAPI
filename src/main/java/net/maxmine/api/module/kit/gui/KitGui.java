package net.maxmine.api.module.kit.gui;

import net.maxmine.api.Management;
import net.maxmine.api.common.gui.Gui;
import net.maxmine.api.common.utility.ItemUtil;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.game.Game;
import net.maxmine.api.module.kit.Kit;
import net.maxmine.api.module.kit.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitGui extends Gui {

    public KitGui(Player player) {
        super(player, "Киты", 6);
    }

    @Override
    protected void drawInventory() {
        int slot = 11;

        Gamer gamer = Management.getGamerLoader().getGamer(this.getPlayer());

        for (Kit kit : KitManager.getManager().getAllKit()) {

            ItemStack item = ItemUtil.getBuilder(kit.getItem(gamer))
                    .setGlowing(gamer.getKit() != null && gamer.getKit() == kit)
                    .build();

            addItem( slot, item,player -> {
                if(kit.isAvailable(gamer)) {
                    gamer.setKit(kit);
                    player.sendMessage(Game.getInstance().getGameType().getPrefix() + "Вы успешно выбрали набор '" + kit.getDisplayName().toLowerCase() + "§f' Когда игра начнётся он будет выдан");

                } else {

                    if(kit.buy(gamer))
                        gamer.setKit(kit);
                }

            });

            if((slot % 9) == 7) slot+=3;
            else slot += 1;
        }
    }
}