package net.maxmine.api.coreclient.packets;

import io.netty.channel.Channel;
import net.maxmine.api.coreclient.buffer.PacketBuffer;

import java.io.IOException;

public abstract class Packet {

    public abstract void write(PacketBuffer packetBuffer) throws IOException;

    public abstract void handle(PacketBuffer packetBuffer) throws IOException;

    public abstract void process(Channel channel) throws IOException;
}
