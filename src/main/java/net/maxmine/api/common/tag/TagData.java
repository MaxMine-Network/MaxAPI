package net.maxmine.api.common.tag;

import net.maxmine.api.common.packet.packetwrapper.WrapperPlayServerScoreboardTeam;
import net.maxmine.api.common.utility.TagUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

@SuppressWarnings("all")
public class TagData {

    private TagPacket packet;

    public TagData(String name, String prefix, String suffix) {
        this.packet = new TagPacket(TagUtil.fixTeam(name), WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);

        setPrefix(prefix);
        setSuffix(suffix);

        this.packet.insertData(this);
    }

    public TagPacket getPacket() {
        return this.packet;
    }

    public String getName() {
        return this.getPacket().getName();
    }

    public boolean hasPlayer(Player player) {
        return getPacket().hasPlayer(player);
    }

    public void addPlayer(Player player) {
        getPacket().addPlayer(player, this);
    }

    public void removePlayer(Player player) {
        getPacket().removePlayer(player, this);
        TagHandler.getTagManager().checkData(this);
    }

    public Collection<Player> getPlayers() {
        return this.getPacket().getPlayers();
    }

    public String getPrefix() {
        return packet.unsafe().getPrefix().toString();
    }

    public String getSuffix() {
        return packet.unsafe().getSuffix().toString();
    }

    public void setPrefix(String prefix) {
        packet.unsafe().setPrefix(TagUtil.fix(prefix));
    }

    public void setSuffix(String suffix) {
        packet.unsafe().setSuffix(TagUtil.fix(suffix));
    }

    public void setPacket(TagPacket packet) {
        this.packet = packet;
    }

    public void destroy() {
        Bukkit.getOnlinePlayers().forEach(this::destroy);
    }

    public void destroy(Player player) {
        TagPacket packet = new TagPacket(getName(), 1);
        packet.insertData(this);
        packet.send(player);
    }

    public void remove() {
        TagHandler.getTagManager().removeTeam(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TagData)) {
            return false;
        }
        TagData data = (TagData)obj;
        return this.getName().equals(data.getName()) && this.getPrefix().equals(data.getPrefix()) && this.getSuffix().equals(data.getSuffix());
    }

    public boolean equals(String name, String prefix, String suffix) {
        return this.getName().equals(TagUtil.fixTeam(name)) && this.getPrefix().equals(prefix) && this.getSuffix().equals(suffix);
    }

    @Override
    public String toString() {
        return "TagData@[name=" + this.getName() + ", prefix=" + this.getPrefix() + ", suffix=" + this.getSuffix() + ", members=" + this.getPacket().unsafe().getPlayers();
    }
}
