package net.maxmine.api.game.listener.chest;

import lombok.Setter;
import net.maxmine.api.common.listener.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ChestListener extends GameListener {

    private ThreadLocalRandom random = ThreadLocalRandom.current();

    private List<ChestItem> items = new ArrayList<>();
    private List<ChestItem> centerItems = new ArrayList<>();

    @Setter
    private List<Location> centerChests = new ArrayList<>();

    private static List<Location> filled = new ArrayList<>();
    private List<Location> playerPlaced = new ArrayList<>();

    public ChestListener() { }

    public ChestListener(Map<ItemStack, Double> items) {
        for (ItemStack item : items.keySet()) {
            ChestItem i = new ChestItem(item, items.get(item), item.getEnchantments());
            if(i.getChance() > 0.1)
                this.items.add(i);
            else
                this.centerItems.add(i);
        }

        Bukkit.getLogger().info("Item listener loaded - " + items.size() + " items");
    }

    public void addCenterChest(Location location) {
        centerChests.add(location);
    }

    public void addItem(ItemStack item, double chance) {
        if(chance > 0.1)
            items.add(new ChestItem(item, chance, item.getEnchantments()));
        else
            centerItems.add(new ChestItem(item, chance, item.getEnchantments()));
    }

    public static void reset() {
        filled.clear();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.TRAPPED_CHEST)
            playerPlaced.add(e.getBlock().getLocation());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
            Location loc = e.getClickedBlock().getLocation();
            if(!filled.contains(loc) && !playerPlaced.contains(loc)) fill((Chest) e.getClickedBlock().getState(), e.getPlayer());
        }
    }

    private void fill(Chest chest, Player player) {
        Inventory inventory = chest.getBlockInventory();
        inventory.clear();

        filled.add(chest.getLocation());

        for (int i = 0; i < (random.nextInt(2) + 4); i++) {
            try {
                ItemStack item = getRandomItem(centerChests.contains(chest.getLocation()) ? centerItems : items);
                while (item == null) {
                    item = getRandomItem(centerChests.contains(chest.getLocation()) ? centerItems : items);
                }
                if(inventory.contains(item.getType())) {
                    i--;
                    continue;
                }

                int slot = random.nextInt(inventory.getSize());
                inventory.setItem(slot, item);
            } catch (Exception ingored) {}
        }
    }

    private ItemStack getRandomItem(List<ChestItem> items) {
        while (true) {
            ChestItem item;

            do {
                item = items.get(random.nextInt(items.size()));
            } while (this.random.nextDouble() < item.getChance());

            if(item.hasEnchants() && this.random.nextDouble() > 0.3) {
                return new ItemStack(item.getItemStack().getType(), item.getItemStack().getAmount(), item.getItemStack().getDurability());
            }

            return item.getItemStack();
        }
    }
}
