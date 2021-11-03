package net.maxmine.api.coreclient.packets.server;

import io.netty.channel.Channel;
import lombok.Getter;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;

import java.io.IOException;

@Getter
public class BranchOnlinePacket extends Packet {

    private String branch;
    private int online;

    public BranchOnlinePacket() {} //Это обязательно!!

    public BranchOnlinePacket(String branch) {
        this.branch = branch;
    }

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(153);
        packetBuffer.writeString(branch);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {
        this.online = packetBuffer.readIntLE();
    }

    @Override
    public void process(Channel channel) throws IOException {

    }
}
