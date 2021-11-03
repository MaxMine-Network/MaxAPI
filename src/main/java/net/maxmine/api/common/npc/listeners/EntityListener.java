package net.maxmine.api.common.npc.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.maxmine.api.Management;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.listener.GameListener;
import net.maxmine.api.common.npc.FakePlayer;
import net.maxmine.api.common.utility.TimerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityListener extends GameListener {

    private List<String> clicks = new ArrayList<>();

    public EntityListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MaxAPI.getInstance(),
                ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();

                if(clicks.contains(player.getName())) return;

                int id = packet.getIntegers().read(0);
                if(id < 0) return;

                FakePlayer fakePlayer = Management.getNPC().getNpc(id);
                if(fakePlayer == null) return;
                fakePlayer.onClick(player);
                clicks.add(player.getName());

                TimerUtil.startTask(() -> clicks.remove(player.getName()), 10L);
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (FakePlayer player : Management.getNPC().getNpcs().values()) {
            player.send(e.getPlayer());
        }
    }
}
