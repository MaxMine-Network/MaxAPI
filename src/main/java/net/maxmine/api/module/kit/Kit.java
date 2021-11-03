package net.maxmine.api.module.kit;

import lombok.Getter;
import net.maxmine.api.game.enums.GameType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Kit extends GameItem {

    private List<ItemStack> itemStacks = new ArrayList<>();

    private final KitManager manager = KitManager.getManager();

    public Kit(String name, GameType type, ItemStack item) {
        super(name, type, item);

        manager.setKit(this);
    }

    public void addItem(ItemStack itemStack, int amount) {
        itemStack.setAmount(amount);
        itemStacks.add(itemStack);
    }

    public void setLore(String... lores) {
        setLore(Arrays.asList(lores));
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}