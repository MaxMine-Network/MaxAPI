package net.maxmine.api.common.tag;

import com.google.common.collect.Sets;
import net.maxmine.api.common.packet.packetwrapper.WrapperPlayServerScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class TagPacket {

    private WrapperPlayServerScoreboardTeam packet;

    /* ------------------------------------------------- */
    private String name;
    private int mode;
    private Collection<Player> players = Sets.newHashSet();
    /* ------------------------------------------------- */

    public TagPacket(String name, int mode) {
        this.name = name;
        this.mode = mode;

        this.packet = new WrapperPlayServerScoreboardTeam();

        this.packet.setName(name);
        this.packet.setDisplayName(name);
        this.packet.setMode(mode);
        setNameTagVisibility(NameTagVisibility.ALWAYS);
    }

    public TagPacket(String name, int mode, Player player) {
        this(name, mode);
        players.add(player);
        packet.getPlayers().add(player.getName());
    }

    public TagPacket(String name, int mode, Collection<Player> players) {
        this(name, mode);
        this.players.addAll(players);
        packet.getPlayers().addAll(players.stream().map(HumanEntity::getName).collect(Collectors.toList()));
    }

    public void insertData(TagData data) {
        this.packet.setPrefix(data.getPrefix());
        this.packet.setSuffix(data.getSuffix());
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    @Deprecated
    public void addPlayer(Player player) {
        addPlayer(player, null, true);
    }

    public void addPlayer(Player player, TagData data) {
        addPlayer(player, data, true);
    }

    public void addPlayer(Player player, TagData data, boolean sendPacket) {
        players.add(player);
        packet.getPlayers().add(player.getName());

        if(!sendPacket) return;
        TagPacket packet = new TagPacket(getName(), WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED, player);
        if(data != null) packet.insertData(data);
        packet.send();
    }

    @Deprecated
    public void removePlayer(Player player) {
        removePlayer(player, null, true);
    }

    public void removePlayer(Player player, TagData data) {
        removePlayer(player, data, true);
    }

    public void removePlayer(Player player, TagData data, boolean sendPacket) {
        players.remove(player);
        packet.getPlayers().remove(player.getName());

        if(!sendPacket) return;
        TagPacket packet = new TagPacket(getName(), WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED, player);
        if(data != null) packet.insertData(data);
        packet.send();
    }

    public void setNameTagVisibility(NameTagVisibility value) {
        packet.setNameTagVisibility(value.toString());
    }

    public NameTagVisibility getNameTagVisibility() {
        return NameTagVisibility.valueOf(packet.getNameTagVisibility());
    }

   /* public void setCanSeeFriendlyInvisibles(boolean value) {
        packet.setCanSeeFriendlyInvisibles(value);
    }

    public boolean canSeeFriendlyInvisibles() {
        return packet.canSeeFriendlyInvisibles();
    }

    public boolean allowFriendlyFire() {
        return packet.allowFriendlyFire();
    }

    public void setAllowFriendlyFire(boolean value) {
        packet.setAllowFriendlyFire(value);
    } */

    public Collection<Player> getPlayers() {
        return players;
    }

    public Collection<String> getPacketMembers() {
        return unsafe().getPlayers();
    }

    public String getName() {
        return name;
    }

    public void send(Player player) {
        packet.sendPacket(player);
    }

    public void sendToTeamMembers() {
        players.forEach(this::send);
    }

    public void update(TagData data) {
        TagPacket packet = new TagPacket(getName(), WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED, getPlayers());
        if(data != null) packet.insertData(data);
        packet.send();
    }

    public void send() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public TagData constructData() {
        TagData data = new TagData(unsafe().getName(), unsafe().getPrefix().toString(), unsafe().getSuffix().toString());
        data.setPacket(this);
        return data;
    }

    public WrapperPlayServerScoreboardTeam unsafe() {
        return packet;
    }

    public int getMode() {
        return mode;
    }

    public enum NameTagVisibility {
        ALWAYS("always"),
        NEVER("never"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam");

        String s;

        NameTagVisibility(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}
