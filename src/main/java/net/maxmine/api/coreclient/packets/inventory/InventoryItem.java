package net.maxmine.api.coreclient.packets.inventory;

import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.common.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuppressWarnings("all")
public class InventoryItem {
    private Material material;
    private String displayName, headKey;
    private short data;
    private int amount = 1;
    private boolean glowing;

    private List<String> lore = new ArrayList<>();
    private final Map<String, Integer> enchantments = new HashMap<>();

    public ItemStack getItemStack() {
        ItemUtil builder = ItemUtil.getBuilder(getMaterial())
                .setAmount(getAmount())
                .setName(getDisplayName())
                .setDurability(getData())
                .setKeySkin(getHeadKey())
                .setLore(getLore())
                .setGlowing(isGlowing());

        enchantments.forEach((enchantment, level) -> builder.addEnchantment(Enchantment.getByName(enchantment), level));

        return builder.build();
    }
}
