package net.maxmine.api.coreclient.packets.party;

import io.netty.channel.Channel;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetPartyPacket extends Packet {

    private String player;
    private List<Player> players = new ArrayList<>();

    public GetPartyPacket() { }

    public GetPartyPacket(String player) {
        this.player = player;
    }

    @Override
    public void write(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeIntLE(1236);
        packetBuffer.writeString(player);
    }

    @Override
    public void handle(PacketBuffer packetBuffer) throws IOException {
        int count = packetBuffer.readIntLE();

        for (int i = 0; i < count; i++) {
            players.add(Bukkit.getPlayer(packetBuffer.readString(16)));
        }
    }

    @Override
    public void process(Channel channel) throws IOException {
        if(Team.getTeam() != null) {
            Team.getTeam().mergePlayersInTeams(players);
        }
    }
}
