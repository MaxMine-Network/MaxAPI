package net.maxmine.api.common.gui.listener;

import net.maxmine.api.common.gui.PagedGui;
import net.maxmine.api.common.gui.item.GuiItem;
import net.maxmine.api.common.listener.GameListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class PagedGuiListener extends GameListener {

    private PagedGui pagedGui;

    public PagedGuiListener(PagedGui pagedGui) {
        this.pagedGui = pagedGui;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(pagedGui.getInventory())) return;
        if (event.isCancelled()) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        event.setCancelled(true);

        int clickedSlot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();

        GuiItem guiItem = pagedGui.getButtonTable().get(pagedGui.getCurrentPage(), clickedSlot);

        if (guiItem != null) {
            guiItem.getClickAction().onClick(player);
        }
    }

}
