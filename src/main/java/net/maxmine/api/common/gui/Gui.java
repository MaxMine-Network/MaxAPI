package net.maxmine.api.common.gui;

import lombok.Getter;
import net.maxmine.api.common.gui.action.ClickAction;
import net.maxmine.api.common.gui.api.GuiAPI;
import net.maxmine.api.common.gui.item.GuiItem;
import net.maxmine.api.common.gui.listener.GuiListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public abstract class Gui implements GuiAPI {

    private final Map<Integer, GuiItem> cache = new HashMap<>();

    @Getter
    private Inventory inventory;

    @Getter
    private Player player;

    public Gui(Player player, String name, int rows) {
        this.inventory = Bukkit.createInventory(null,  rows * 9, name);
        this.player = player;

        new GuiListener(this);
    }

    protected void drawInventory() {

    }

    public void addItem(int slot, ItemStack itemStack, ClickAction clickAction) {
        cache.put(slot, new GuiItem(slot, itemStack, clickAction));
    }

    @Override
    public void prepareInventory() {
        for (GuiItem guiItem : getItems()) {
            this.inventory.setItem(guiItem.getSlot(), guiItem.getItemStack());
        }
    }

    @Override
    public void clearInventory() {
        this.inventory.clear();
    }

    @Override
    public void clearButtons() {
        cache.clear();
    }

    @Override
    public void openGui() {
        drawInventory();
        prepareInventory();

        player.openInventory(this.inventory);
    }

    private Collection<GuiItem> getItems() {
        return cache.values();
    }

    public GuiItem getGuiItem(int slot) {
        return cache.get(slot);
    }
}
