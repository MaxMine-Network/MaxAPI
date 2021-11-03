package net.maxmine.api.coreclient.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.maxmine.api.coreclient.callback.CoreCallback;
import net.maxmine.api.coreclient.connection.CoreConnection;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.coreclient.packets.game.GetKitPacket;
import net.maxmine.api.coreclient.packets.inventory.InventoryPacket;
import net.maxmine.api.coreclient.packets.party.GetPartyPacket;
import net.maxmine.api.coreclient.packets.player.GetDataPacket;
import net.maxmine.api.coreclient.packets.player.PlayerSkinChangePacket;
import net.maxmine.api.coreclient.packets.server.BranchOnlinePacket;
import net.maxmine.api.coreclient.packets.shared.GetSharedOnline;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketManager {

    private static final ExecutorService QUERY_EXECUTOR = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("core-thread #%s")
                    .setDaemon(true)
                    .build());

    private static Map<Integer, Class<? extends Packet>> packets = new HashMap<>();

    static {
        packets.put(110, InventoryPacket.class);
        packets.put(153, BranchOnlinePacket.class);
        packets.put(1500, GetDataPacket.class);
        packets.put(1234, GetSharedOnline.class);
        packets.put(1236, GetPartyPacket.class);
        packets.put(1505, PlayerSkinChangePacket.class);
        packets.put(1004, GetKitPacket.class);
    }

    public static boolean hasPacket(int id) {
        return packets.containsKey(id);
    }

    public static Packet getPacket(int id) {
        try {
            return packets.get(id).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static void sendPacket(Packet packet) {
        if(!CoreConnection.channel.isActive()) {
            return;
        }
        CoreConnection.channel.writeAndFlush(packet);
    }

    public static void sendPacket(Packet packet, CoreCallback callback) {
        CoreConnection.responses.put(packet.getClass(), callback);
        sendPacket(packet);
    }

    public static void registerPacket(int id, Class<? extends Packet> clazz) {
        packets.put(id, clazz);
    }
}
