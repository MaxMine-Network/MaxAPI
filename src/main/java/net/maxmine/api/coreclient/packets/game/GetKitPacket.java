package net.maxmine.api.coreclient.packets.game;

import io.netty.channel.Channel;
import lombok.Getter;
import net.maxmine.api.Management;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.module.kit.Kit;
import net.maxmine.api.module.kit.KitManager;

import java.io.IOException;

@Getter
public class GetKitPacket extends Packet {

    private String playerName;
    private String gameName;

    private String kit;

    public GetKitPacket() {}

    public GetKitPacket(String player, String game) {
        this.playerName = player;
        this.gameName = game;
    }

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(1003);
        packetBuffer.writeString(playerName);
        packetBuffer.writeString("KIT_" + gameName.toUpperCase());
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {
        this.playerName = packetBuffer.readString(16);
        packetBuffer.readString(128);
        this.kit = packetBuffer.readString(128);
    }

    @Override
    public void process(Channel channel) throws IOException {
        Gamer gamer = Management.getGamerLoader().getGamer(playerName);
        Kit kit = KitManager.getManager().getKit(this.kit);

        if(!this.kit.isEmpty() && gamer != null && kit != null)
            gamer.setKit(kit);
    }
}
