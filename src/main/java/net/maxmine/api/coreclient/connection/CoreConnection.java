package net.maxmine.api.coreclient.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.callback.CoreCallback;
import net.maxmine.api.coreclient.manager.PacketManager;
import net.maxmine.api.coreclient.packets.Core;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.game.Game;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.LinkedHashMap;

public class CoreConnection extends SimpleChannelInboundHandler<Packet> {

    public static final LinkedHashMap<Class<? extends Packet>, CoreCallback> responses = new LinkedHashMap<>();
    public static Channel channel;
    public static Thread onlineUpdaterThread;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        CoreConnection.channel = ctx.channel();

        PacketManager.sendPacket(getServerPacket());

        if(onlineUpdaterThread == null) {
            onlineUpdaterThread = new Thread(() -> {
                do {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(channel.isActive())
                        PacketManager.sendPacket(getOnlinePacket());
                    else
                        Core.reconect();
                } while (true);
            });

            onlineUpdaterThread.start();
        }


        Game game = Game.getInstance();

        if(game != null) {
            game.setStatus(game.getState());
            game.sendMapPacket();
        }
    }

    public static void disconnect() {
        String serverName = Bukkit.getServerName();

        Packet packet = new Packet() {
            @Override
            public void write(PacketBuffer packetBuffer) throws IOException {
                packetBuffer.writeIntLE(155);
                packetBuffer.writeString(serverName);
            }

            @Override
            public void handle(PacketBuffer packetBuffer) throws IOException {

            }

            @Override
            public void process(Channel channel) throws IOException {

            }
        };

        PacketManager.sendPacket(packet);
    }

    private Packet getOnlinePacket() {
        return new Packet() {
            @Override
            public void write(PacketBuffer packetBuffer) throws IOException {
                packetBuffer.writeIntLE(151);
                packetBuffer.writeString(Bukkit.getServerName());
                packetBuffer.writeIntLE(Bukkit.getOnlinePlayers().size());
            }

            @Override
            public void handle(PacketBuffer packetBuffer) throws IOException {

            }

            @Override
            public void process(Channel channel1) throws IOException {

            }
        };
    }

    private Packet getServerPacket() {
        return new Packet() {
            @Override
            public void write(PacketBuffer packetBuffer) throws IOException {
                packetBuffer.writeIntLE(0);
                packetBuffer.writeIntLE(1);
                packetBuffer.writeString(Bukkit.getServerName());
                packetBuffer.writeString(Bukkit.getIp());
                packetBuffer.writeIntLE(Bukkit.getPort());
            }

            @Override
            public void handle(PacketBuffer packetBuffer) throws IOException {

            }

            @Override
            public void process(Channel channel) throws IOException {

            }
        };

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        Packet packet = (Packet) o;

        if(responses.containsKey(packet.getClass())) {
            CoreCallback callback = responses.remove(packet.getClass());

            packet.process(ctx.channel());
            callback.done(packet);
        } else {
            packet.process(ctx.channel());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet o) throws Exception {

    }
}
