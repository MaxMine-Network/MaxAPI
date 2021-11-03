package net.maxmine.api.coreclient.packets.player;

import io.netty.channel.Channel;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;

import java.io.IOException;

public class PlayerRedirectPacket extends Packet {

    private String player;
    private String server;

    public PlayerRedirectPacket(String player, String server) {
        this.player = player;
        this.server = server;
    }

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(102);
        packetBuffer.writeString(player);
        packetBuffer.writeString(server);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {

    }

    @Override
    public void process(Channel channel) throws IOException {

    }
}
