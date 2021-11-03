package net.maxmine.api.coreclient.packets.player;

import io.netty.channel.Channel;
import net.maxmine.api.common.skin.PlayerSkins;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;

import java.io.IOException;

public class PlayerSkinChangePacket extends Packet {

    private String player, skin;

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {

    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {
        this.player = packetBuffer.readString(16);
        this.skin = packetBuffer.readString(16);
    }

    @Override
    public void process(Channel channel) throws IOException {
        PlayerSkins.applySkin(player, skin);
    }
}