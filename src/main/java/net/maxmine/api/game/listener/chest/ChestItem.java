package net.maxmine.api.game.listener.chest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ChestItem {

    private ItemStack itemStack;
    private double chance;
    private Map<Enchantment, Integer> enchantments;

    public boolean hasEnchants() {
        return enchantments.size() > 0;
    }

}
