package net.maxmine.api.coreclient.packets.shared;

import io.netty.channel.Channel;
import lombok.Getter;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;

import java.io.IOException;

public class GetSharedOnline extends Packet {

    @Getter
    private int online;

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(1234);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {
        this.online = packetBuffer.readIntLE();
    }

    @Override
    public void process(Channel channel) throws IOException {

    }
}
