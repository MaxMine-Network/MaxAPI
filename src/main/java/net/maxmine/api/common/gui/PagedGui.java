package net.maxmine.api.common.gui;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.common.gui.action.ClickAction;
import net.maxmine.api.common.gui.api.GuiAPI;
import net.maxmine.api.common.gui.item.GuiItem;
import net.maxmine.api.common.gui.listener.PagedGuiListener;
import net.maxmine.api.common.utility.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public abstract class PagedGui implements GuiAPI {

    private static final ItemStack NEXT_PAGE = ItemUtil.getBuilder(Material.ARROW)
            .setName("§aСледующая страница")
            .setLore("",
                    "§7Нажмите для перехода на",
                    "§7следующую страницу")
            .build();
    private static final ItemStack PREV_PAGE = ItemUtil.getBuilder(Material.ARROW)
            .setName("§cПредыдущая страница")
            .setLore("",
                    "§7Нажмите для перехода на",
                    "§7предыдущую страницу")
            .build();

    @Getter
    private Table<Integer, Integer, GuiItem> buttonTable = HashBasedTable.create();

    private String name;

    private int rows;
    @Getter
    private Player player;
    @Getter
    private Inventory inventory;

    @Getter
    @Setter
    private int currentPage = 1;

    public PagedGui(Player player, String name, int rows) {
        this.inventory = Bukkit.createInventory(null,  rows * 9, name);
        this.player = player;
        this.rows = rows * 9;
        this.name = name;

        new PagedGuiListener(this);
    }

    protected void drawInventory() {

    }

    public void addItem(int page, int slot, ItemStack itemStack, ClickAction clickAction) {
        buttonTable.put(page, slot, new GuiItem(slot, itemStack, clickAction));
    }

    @Override
    public void prepareInventory() {
        boolean hasNextPage = buttonTable.containsRow(currentPage + 1);
        boolean hasPrevPage = currentPage > 1;

        int lastRow = this.rows - 1;
        int firstRow = lastRow - 8;

        if (hasNextPage)
            addItem(currentPage, lastRow, NEXT_PAGE, (player) -> openGui(currentPage + 1));
        if (hasPrevPage)
            addItem(currentPage, firstRow, PREV_PAGE,  player -> openGui(currentPage - 1));

        for (GuiItem guiItem : getItems(currentPage)) {
            int slot = guiItem.getSlot();

            if (slot > inventory.getSize())
                inventory = Bukkit.createInventory(null, getMaxSize(slot), name);

            inventory.setItem(slot, guiItem.getItemStack());
        }

    }

    @Override
    public void clearInventory() {
        this.inventory.clear();
    }

    @Override
    public void clearButtons() {
        buttonTable.clear();
    }

    @Override
    public void openGui() {

    }

    public void openGui(int currentPage) {
        this.currentPage = currentPage;

        inventory.clear();

        drawInventory();
        prepareInventory();

        player.openInventory(this.inventory);
    }

    public void openPagedGui() {
        this.openGui(1);
    }

    private int getMaxSize(int slot) {
        if (slot >= 0 && slot < 9) return 9;
        else if (slot >= 9 && slot < 18) return 27;
        else if (slot >= 18 && slot < 27) return 36;
        else if (slot >= 27 && slot < 35) return 45;
        else if (slot >= 35 && slot < 46) return 54;
        else return 54;
    }

    private Collection<GuiItem> getItems(int page) {
        return buttonTable
                .rowMap()
                .getOrDefault(page, Maps.newHashMap())
                .values();
    }
}
