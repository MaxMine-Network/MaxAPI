package net.maxmine.api.module.kit;

import lombok.Getter;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.common.utility.ItemUtil;
import net.maxmine.api.module.kit.gui.KitGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitManager extends GameListener {

    private final static ItemStack item = ItemUtil.getBuilder(Material.CHEST)
            .setName("§eНаборы")
            .build();

    @Getter
    private static KitManager manager;

    public KitManager() {
        manager = this;
    }

    @Getter
    private Map<String, Kit> kits = new HashMap<>();

    @Getter
    private int LAST_ID = 0;

    public void setKit(Kit kit) {
        kits.put(kit.getName(), kit);
    }

    public Kit getKit(String key) {
        return kits.getOrDefault(key, null);
    }

    public List<Kit> getAllKit() {
        return new ArrayList<>(kits.values());
    }

    public static void giveItem(Player player) {
        player.getInventory().setItem(0, item);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getType() == Material.CHEST && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eНаборы")) {
            new KitGui(e.getPlayer()).openGui();
        }
    }
}
