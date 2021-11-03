package net.maxmine.api.coreclient.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.manager.PacketManager;
import net.maxmine.api.coreclient.packets.Packet;
import org.bukkit.Bukkit;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() != 0) {
            PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

            int packetID = packetBuffer.readIntLE();

            if(PacketManager.hasPacket(packetID)) {
                Packet packet = PacketManager.getPacket(packetID);

                if(packet == null) {
                    Bukkit.getLogger().info("Packet null, lol (" + packetID + ")");
                    return;
                }

                packet.handle(packetBuffer);
                list.add(packet);
            }
        }
    }
}