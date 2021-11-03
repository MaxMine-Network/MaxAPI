package net.maxmine.api.coreclient.packets;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import net.maxmine.api.coreclient.codec.Decoder;
import net.maxmine.api.coreclient.codec.Encoder;
import net.maxmine.api.coreclient.connection.CoreConnection;
import net.maxmine.api.game.Game;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;

public class Core {

    public static boolean connected;
    @Getter
    private static Thread connectionThread;
    private static Bootstrap client;
    private static ChannelFuture channel;

    public Core() {
        connect();
    }

    public static void reconect() {
        connected = false;
        //connect();
    }

    private static void connect() {
        connectionThread = new Thread(() -> {
            while (!connectionThread.isInterrupted()) {
                EventLoopGroup group = new NioEventLoopGroup();

                client = new Bootstrap();

                client.group(group)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(new InetSocketAddress("localhost", 1488))
                        .option(ChannelOption.SO_RCVBUF, 1048576)
                        .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1048576))
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel channel) {
                                channel.pipeline().addLast("decoder", new Decoder());
                                channel.pipeline().addLast("encoder", new Encoder());
                                channel.pipeline().addLast("handler", new CoreConnection());
                            }
                        });

                try {
                    channel = client.connect();
                    connected = true;
                    if(Game.getInstance() != null) {
                        Game.getInstance().sendMapPacket();
                        Game.getInstance().setStatus(Game.getInstance().getState());
                    }

                    Bukkit.getLogger().info("Connected to Core");
                    channel.sync();
                    channel.channel().closeFuture().sync();
                } catch (Exception e) {
                    Bukkit.getLogger().info("Ошибка соединения с Core - " + e.getLocalizedMessage());
                    connected = false;
                }
                wait(2000);
            }
        });

        connectionThread.start();
    }

    private static void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
