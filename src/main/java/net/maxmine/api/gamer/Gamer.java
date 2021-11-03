package net.maxmine.api.gamer;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.maxmine.api.Management;
import net.maxmine.api.common.adapter.Adapter;
import net.maxmine.api.common.adapter.BoosterAdapter;
import net.maxmine.api.common.adapter.EconomyAdapter;
import net.maxmine.api.common.adapter.PurchaseAdapter;
import net.maxmine.api.common.utility.LoggerUtil;
import net.maxmine.api.coreclient.buffer.PacketBuffer;
import net.maxmine.api.coreclient.manager.PacketManager;
import net.maxmine.api.coreclient.packets.Packet;
import net.maxmine.api.coreclient.packets.command.ExecuteCommandPacket;
import net.maxmine.api.coreclient.packets.player.PlayerRedirectPacket;
import net.maxmine.api.game.Game;
import net.maxmine.api.game.enums.GameState;
import net.maxmine.api.gamer.booster.Booster;
import net.maxmine.api.gamer.group.Group;
import net.maxmine.api.gamer.level.LevelManager;
import net.maxmine.api.gamer.level.Leveling;
import net.maxmine.api.gamer.stats.PersonalStats;
import net.maxmine.api.module.event.LevelUpEvent;
import net.maxmine.api.module.kit.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@Getter
public class Gamer {

    private final String name;
    private final Group group;

    @Setter
    private Player player;
    private Kit kit;

    @Getter
    @Setter
    private EntityDamageEvent.DamageCause damageCause;

    @Getter
    @Setter
    private boolean spectator;

    private Map<String, Object> data = new HashMap<>();

    public Gamer(String name) {
        long start = System.currentTimeMillis();

        this.name = name;
        this.group = Management.getGroupAdapter().getOfflinePlayerGroup(name);

        LoggerUtil.info("[GamerLoader] Gamer %s has been loaded (%d ms)", name, System.currentTimeMillis() - start);
    }

    public void addXp(double amount) {
        double current = getXp();
        double level = getLevel();

        if((current + amount) >= Leveling.getExpFromLevelToNext(level)) {
            LevelUpEvent event = new LevelUpEvent(this, level, level+1);
            Bukkit.getPluginManager().callEvent(event);
        }

        data.replace(Leveling.EXP_FIELD, current + amount);
    }

    public int changeMoney(int change) {
        if (change > 0 && getBooster() != null)
            change *= getBooster().getMultiplier();

        getEconomyAdapter().change(change);

        if (change > 0) sendActionBar("§c+" + change);
        else sendActionBar("§c" + change);

        return change;
    }


    public void save() {
        data.values().forEach(object -> {
            if (object instanceof Adapter)
                ((Adapter) object).save();
        });

        if(getXp() > 0) {
            LevelManager.saveExp(name, getXp());
        }
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public void addDataInt(String key, int add) {
        data.put(key, getDataInt(key) + add);
    }

    public void addDataLong(String key, long add) {
        data.put(key, getDataLong(key) + add);
    }

    public void setDataLong(String key, long add) {
        data.put(key, add);
    }

    public void sendActionBar(String text) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
    }

    public void setKit(Kit kit) {
        this.kit = kit;

        Packet packet = new Packet() {
            @Override
            public void write(PacketBuffer packetBuffer) throws IOException {
                packetBuffer.writeIntLE(1005);
                packetBuffer.writeString(name);

                packetBuffer.writeString("KIT_" + kit.getType().name());
                packetBuffer.writeString(kit.getName());
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


    public void executeCoreCommand(String command, String[] args) {
        ExecuteCommandPacket packet = new ExecuteCommandPacket(this.name, command, args);
        PacketManager.sendPacket(packet);
    }

    public void redirect(String server) {
        PlayerRedirectPacket packet = new PlayerRedirectPacket(name, server);

        PacketManager.sendPacket(packet);
    }

    public boolean isInGame() {
        return Game.getInstance() != null && Game.getInstance().getState() != GameState.GAME && !isSpectator();
    }

    public double getXp() {
        return (double) data.computeIfAbsent(Leveling.EXP_FIELD, f -> LevelManager.loadExp(name));
    }

    public BoosterAdapter getBoosterAdapter() {
        return (BoosterAdapter) data.computeIfAbsent(BoosterAdapter.BOOSTER_FIELD, f -> new BoosterAdapter(name));
    }

    public EconomyAdapter getEconomyAdapter() {
        return (EconomyAdapter) data.computeIfAbsent(EconomyAdapter.ECONOMY_FIELD, f -> new EconomyAdapter(name));
    }

    public PurchaseAdapter getPurchaseAdapter() {
        return (PurchaseAdapter) data.computeIfAbsent(PurchaseAdapter.PURCHASE_FIELD, f -> new PurchaseAdapter(this));
    }

    public PersonalStats getPersonalStats() {
        return (PersonalStats) data.computeIfAbsent(PersonalStats.STATS_FIELD, f -> new PersonalStats(name.toLowerCase()));
    }

    public List<Booster> getBoosters() {
        return getBoosterAdapter().getBoosters();
    }

    public Booster getBooster() {
        return getBoosterAdapter().getActiveBooster();
    }

    public boolean getBoolean(String key) {
        return (boolean) data.putIfAbsent(key, false);
    }

    public Object getData(String key) {
        return data.getOrDefault(key, null);
    }

    public int getDataInt(String key) {
        return (int) data.computeIfAbsent(key, f -> 0);
    }

    public long getDataLong(String key) {
        return (long) data.computeIfAbsent(key, f -> 0L);
    }

    public int getMultiplier() {
        return Management.getGroupAdapter().getMultiplier(this);
    }

    public int getLevel() {
        return (int) Leveling.getLevel(getXp());
    }

    public int getMoney() {
        return getEconomyAdapter().getMoney();
    }

}
