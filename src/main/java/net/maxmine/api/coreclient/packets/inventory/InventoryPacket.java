package net.maxmine.api.coreclient.packets.inventory;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class InventoryPacket extends Packet {

    private final Gson GSON = new Gson();

    private String name, playerName;
    private int slots;
    private int buttonsCount;

    private Map<Integer, InventoryItem> buttons = new HashMap<>();

    public InventoryPacket() {

    }

    @Override
    public void write(PacketBuffer packetBuffer) {

    }

    @Override
    public void handle(PacketBuffer packetBuffer) {
        this.name = packetBuffer.readString(128);
        this.slots = packetBuffer.readIntLE();
        this.buttonsCount = packetBuffer.readIntLE();
        this.playerName = packetBuffer.readString(16);

        for (int i = 0; i < buttonsCount; i++) {
            if (packetBuffer.readableBytes() != 0) {
                int slot = packetBuffer.readIntLE();
                int length = packetBuffer.readIntLE();
                String gson = packetBuffer.readString(length);

                InventoryItem item = GSON.fromJson(gson, InventoryItem.class);
                buttons.put(slot, item);
            }
        }
    }

    @Override
    public void process(Channel channel) {
        Player player = Bukkit.getPlayer(playerName);

        if(player != null) {
            new CoreInventory(player, this.name, this.slots * 9).open(this.buttons);
        }
    }
}
