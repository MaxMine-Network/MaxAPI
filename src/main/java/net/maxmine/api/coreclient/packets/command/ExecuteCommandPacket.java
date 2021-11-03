package net.maxmine.api.coreclient.packets.command;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;

import java.io.IOException;

@AllArgsConstructor
public class ExecuteCommandPacket extends Packet {

    private String player;
    private String command;
    private String[] args;

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(601);
        packetBuffer.writeString(this.player);
        packetBuffer.writeString(this.command);
        packetBuffer.writeIntLE(args.length);

        for (String s : args)
            packetBuffer.writeString(s);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {

    }

    @Override
    public void process(Channel channel) throws IOException {

    }
}
