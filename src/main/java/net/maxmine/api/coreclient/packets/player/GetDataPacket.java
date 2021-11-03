package net.maxmine.api.coreclient.packets.player;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import net.maxmine.api.Management;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.gamer.Gamer;

import java.io.IOException;
import java.util.Map;

public class GetDataPacket extends Packet {

    private static final Gson GSON = new Gson();

    private String player;
    private int length;
    private String json;

    public GetDataPacket() { }

    public GetDataPacket(String player) {
        this.player = player;
    }

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(1499);
        packetBuffer.writeString(player);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {
        this.player = packetBuffer.readString(16);
        this.length = packetBuffer.readIntLE();
        this.json = packetBuffer.readString(length);
    }

    @Override
    public void process(Channel channel) throws IOException {
        Gamer gamer = Management.getGamerLoader().getGamer(player);

        if(gamer != null) {
            Map<String, Boolean> map = GSON.fromJson(json, Map.class);
            map.forEach(gamer::addData);
        }
    }
}
