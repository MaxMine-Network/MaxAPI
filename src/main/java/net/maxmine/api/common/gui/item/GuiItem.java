package net.maxmine.api.common.gui.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.maxmine.api.common.gui.action.ClickAction;
import org.bukkit.inventory.ItemStack;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@Getter
@AllArgsConstructor
public class GuiItem {

    private int slot;
    private ItemStack itemStack;
    private ClickAction clickAction;
}
