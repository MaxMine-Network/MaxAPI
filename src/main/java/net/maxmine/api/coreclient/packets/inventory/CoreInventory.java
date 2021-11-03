package net.maxmine.api.coreclient.packets.inventory;

import com.google.common.collect.Lists;
import net.maxmine.api.Management;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.coreclient.manager.PacketManager;
import net.maxmine.api.gamer.Gamer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class CoreInventory extends GameListener {

    private Player player;

    private List<Integer> buttons = Lists.newArrayList();

    private Inventory inventory;

    public CoreInventory(Player player, String name, int slots) {
        this.player = player;

        this.inventory = Bukkit.createInventory(player, slots, name);
    }

    public void open(Map<Integer, InventoryItem> buttons) {
        Gamer gamer = Management.getGamerLoader().getGamer(player);

        CoreInventory coreInventory = (CoreInventory) gamer.getData("CoreInventory");

        if(coreInventory != null) {
            coreInventory.prepareInventory(buttons);
            player.updateInventory();
            unregister();
        } else {
            prepareInventory(buttons);
            player.openInventory(inventory);
            gamer.addData("CoreInventory", this);
        }
    }

    private void prepareInventory(Map<Integer, InventoryItem> buttons) {
        inventory.clear();
        this.buttons.clear();

        buttons.forEach((slot, item) -> {
            inventory.setItem(slot, item.getItemStack());
            this.buttons.add(slot);
        });
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        Gamer gamer = Management.getGamerLoader().getGamer(player);

        if(inventory.getHolder().equals(player) && inventory.equals(this.inventory)) {
            gamer.getData().remove("CoreInventory");
            unregister();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() != null) {
            Inventory clickedInventory = e.getClickedInventory();

            if(clickedInventory.getHolder().equals(player) && clickedInventory.equals(inventory)) {
                e.setCancelled(true);

                int slot = e.getSlot();

                if(buttons.contains(slot)) {
                    InventoryClickPacket packet = new InventoryClickPacket(player.getName(), slot);

                    PacketManager.sendPacket(packet);
                }
            }
        }
    }

}
