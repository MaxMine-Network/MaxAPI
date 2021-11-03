package net.maxmine.api.coreclient.packets.inventory;

import io.netty.channel.Channel;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;

import java.io.IOException;

public class InventoryClickPacket extends Packet {

    private String player;
    private int slot;

    public InventoryClickPacket(String player, int slot) {
        this.player = player;
        this.slot = slot;
    }

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(120);
        packetBuffer.writeString(player);
        packetBuffer.writeIntLE(slot);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {

    }

    @Override
    public void process(Channel channel) throws IOException {

    }
}
