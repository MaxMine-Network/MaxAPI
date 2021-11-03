package net.maxmine.api.common.gui.listener;

import net.maxmine.api.common.gui.Gui;
import net.maxmine.api.common.gui.item.GuiItem;
import net.maxmine.api.common.listener.GameListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class GuiListener extends GameListener {

    private Gui gui;

    public GuiListener(Gui gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!gui.getInventory().equals(e.getClickedInventory())) {
            return;
        }

        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null) return;

        e.setCancelled(true);

        int slot = e.getRawSlot();
        Player player = (Player) e.getWhoClicked();

        GuiItem guiItem = gui.getGuiItem(slot);

        if (guiItem != null) {
            guiItem.getClickAction().onClick(player);
        }
    }

}
