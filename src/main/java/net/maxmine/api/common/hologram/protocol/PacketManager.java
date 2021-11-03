package net.maxmine.api.common.hologram.protocol;

import com.google.common.base.Preconditions;
import net.maxmine.api.common.hologram.HologramLine;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public class PacketManager {

    public static void spawn(HologramLine line, Player receiver) {
        Preconditions.checkArgument(line instanceof PacketHolder, "Line not instance of PacketHolder");
        for(Packet packet: ((PacketHolder)line).getSpawnPacket()) {
            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket((Packet<?>) packet);
        }
    }

    public static void remove(HologramLine line, Player receiver) {
        Preconditions.checkArgument(line instanceof PacketHolder, "Line not instance of PacketHolder");
        for(Packet packet: ((PacketHolder)line).getRemovePacket()) {
            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void update(HologramLine line, Player receiver) {
        Preconditions.checkArgument(line instanceof PacketHolder, "Line not instance of PacketHolder");
        for(Packet packet: ((PacketHolder)line).getUpdatePacket()) {
            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void velocity(HologramLine line, Vector vector, Player player) {
        Preconditions.checkArgument(line instanceof PacketHolder, "Line not instance of PacketHolder");
        for(Packet packet: ((PacketHolder)line).getVelocityPacket(vector.getX(), vector.getY(), vector.getZ())) {

            update(line, player);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
