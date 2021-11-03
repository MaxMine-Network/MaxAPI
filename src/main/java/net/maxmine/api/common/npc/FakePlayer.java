package net.maxmine.api.common.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.maxmine.api.Management;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.hologram.Hologram;
import net.maxmine.api.common.packet.packetwrapper.WrapperPlayServerScoreboardTeam;
import net.maxmine.api.common.skin.Skin;
import net.maxmine.api.common.utility.MojangUtil;
import net.maxmine.api.common.utility.StringUtil;
import net.maxmine.api.common.utility.TimerUtil;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class FakePlayer {


    private Skin skin;
    private Location location;
    private EntityPlayer npc;

    private List<Hologram> holograms = new ArrayList<>();

    private int entity_id = 0;

    private volatile BukkitTask headLooker;
    private volatile BukkitTask sneaking;

    private boolean sneak = false;

    @Getter
    private static final List<FakePlayer> fakePlayers = new ArrayList<>();

    public FakePlayer(String npcName, String skinName, Location location) {
        this.location = location;
        skin = MojangUtil.getCachedSkinData(skinName);

        if (skin == null)
            skin = MojangUtil.getCachedSkinData("Steve");

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), skinName);
        gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

        npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));

        Player npcPlayer = npc.getBukkitEntity().getPlayer();
        npcPlayer.setPlayerListName("");

        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        npcPlayer.teleport(location);

        Management.getNPC().addNpc(this);

        fakePlayers.add(this);

    }


    public void enableHeadLooker(int delay) {
        this.headLooker = new BukkitRunnable() {
            public void run() {
                Player nearest = null;
                for (Entity player : location.getWorld().getNearbyEntities(location, 3, 3, 3)) {
                    if (player.getWorld() != location.getWorld()) {
                        continue;
                    }
                    if (nearest == null) {
                        nearest = (Player) player;
                    } else {
                        if (nearest.getLocation().distanceSquared(location) < player.getLocation().distanceSquared(location)) {
                            continue;
                        }
                        nearest = (Player) player;
                    }
                }
                if (nearest == null) {
                    return;
                }
                final Vector vector = nearest.getLocation().subtract(location).toVector().normalize();
                final Location location = getLocation();
                location.setDirection(vector);

                npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendPacket(player, new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.lastYaw * 256 / 360)));
                    sendPacket(player, new PacketPlayOutEntityTeleport(npc));

                }
            }
        }.runTaskTimer(MaxAPI.getInstance(), delay, delay);
    }

    public void setSneaking() {
        this.sneaking = new BukkitRunnable() {
            @Override
            public void run() {
                updateData(0, (byte) 0x02, (sneak = !sneak));

                if (sneak) {
                    holograms.forEach(hologram -> hologram.addToLocation(0.0, -0.4, 0.0));
                } else {
                    holograms.forEach(hologram -> hologram.addToLocation(0.0, 0.4, 0.0));
                }
            }
        }.runTaskTimer(MaxAPI.getInstance(), 20L, 20L);
    }

    private void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void updateData(int id, byte packetID, boolean b) {
        DataWatcher dataWatcher = npc.getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.a.a(id), (b ? packetID : 0));

        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(player, new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true));
        }
    }

    public void setName(String name) {
        npc.setCustomName(name);
        npc.setCustomNameVisible(true);
    }

    public void send() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public void send(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        WrapperPlayServerScoreboardTeam scoreMod = new WrapperPlayServerScoreboardTeam(new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM));
        scoreMod.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
        scoreMod.setName(StringUtil.randomString(8));
        scoreMod.setDisplayName(StringUtil.randomString(8));
        scoreMod.setPrefix(npc.getName());
        scoreMod.setSuffix(npc.getName());
        scoreMod.setNameTagVisibility("never");
        scoreMod.setPlayers(Lists.newArrayList(npc.getName()));

        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
        scoreMod.sendPacket(player);
        TimerUtil.startTask(() -> connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)), 30L);
    }

    public abstract void onClick(Player player);

    public void addHologram(String... lines) {
        holograms.add(Hologram.getBuilder(location.clone().add(0.0D, 1.0D, 0.0D))
                .line(lines)
                .build());
    }
}