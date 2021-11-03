package net.maxmine.api.common.tag;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.utility.TagUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("all")
public class TagManager {

    private Collection<TagAdapter> adapters = Sets.newConcurrentHashSet();
    private Set<TagData> datas = Sets.newConcurrentHashSet();

    @Getter
    private static TagManager manager;

    public TagManager() {
        manager = this;
        TagHandler.init(this);
    }

    public boolean updateTag(Player player, String prefix, String suffix) {
        TagData data = getTagData(player);
        if(data == null) return false;
        if(prefix != null) data.setPrefix(prefix);
        if(suffix != null) data.setSuffix(suffix);
        data.getPacket().update(data);
        return true;
    }

    public void setTag(Player player, String prefix) {
        this.setTag(player, prefix, "");
    }

    public void setTag(Player player, String prefix, String suffix) {
        this.setTag(player, player.getName(), prefix, suffix);
    }

    public void registerTeam(TagData data) {
        datas.add(data);
    }

    public void setTeam(Player player, TagData data) {
        synchronized (this) {
            checkTag(player);
        }
        data.addPlayer(player);
    }

    public void setTag(Player player, String team, String prefix, String suffix) {
        team = team.toLowerCase();

        synchronized (this) {
            checkTag(player);
        }
        TagData data = getData(team);
        findDifference(data, team, prefix, suffix);
        if (data == null) {
            data = new TagData(team, prefix, suffix);
            datas.add(data);
            data.getPacket().send();
        }

        synchronized (this) {
            data.addPlayer(player);
        }
    }

    public void checkData(TagData data) {
        if (data.getPlayers().size() <= 0) {
            data.remove();
        }
    }

    private void checkTag(Player player) {
        if (this.hasTag(player)) {
            TagData t = this.getTagData(player);
            t.removePlayer(player);
        }
    }

    public void sendTags(Player player) {
        this.datas.forEach(data -> data.getPacket().send(player));
    }

    public void clearTags() {
        this.datas.forEach(this::removeTeam);
    }

    public void removeTeam(TagData data) {
        data.destroy();
        this.datas.remove(data);
    }

    public void cleanupTeams(String team) {
        datas.stream()
                .filter(data -> data.getName().equalsIgnoreCase(team))
                .forEach(TagData::remove);
    }

    public void clearTag(Player player) {
        if (!this.hasTag(player)) {
            return;
        }
        TagData data = this.getTagData(player);
        data.removePlayer(player);
        checkData(data);
    }

    public boolean hasTag(Player player) {
        return this.getTagData(player) != null;
    }

    public TagData getTagData(Player player) {
        for (TagData data : this.datas) {
            if (!data.hasPlayer(player)) {
                continue;
            }
            return data;
        }
        return null;
    }

    public Collection<TagData> getDatas() {
        return this.datas;
    }

    private void findDifference(TagData data, String team, String prefix, String suffix) {
        if (data == null) {
            return;
        }
        if (data.equals(team, prefix, suffix)) {
            return;
        }
        data.setPrefix(prefix);
        data.setSuffix(suffix);
        data.getPacket().update(data);
    }

    public void registerAdapter(TagAdapter adapter) {
        this.adapters.add(adapter);
        adapter.onEnable();
    }

    public void unregisterAdapter(TagAdapter adapter) {
        this.adapters.remove(adapter);
        adapter.onDisable();
    }

    public void enableAdapters() {
        this.adapters.forEach(TagAdapter::onEnable);
    }

    public void disableAdapters() {
        this.adapters.forEach(TagAdapter::onDisable);
    }

    public void resendData() {
        Bukkit.getOnlinePlayers().forEach(this::resendData);
    }

    public void resendData(Player player) {
        this.getDatas().forEach(data -> data.destroy(player));
        Bukkit.getScheduler().runTaskLater(MaxAPI.getInstance(), () -> this.sendTags(player), 10L);
    }

    public TagData getData(String team) {
        TagData data = null;
        for (TagData td : this.datas) {
            if (!td.getName().equals(TagUtil.fixTeam(team))) {
                continue;
            }
            data = td;
        }
        return data;
    }
}