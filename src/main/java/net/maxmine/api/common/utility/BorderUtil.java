package net.maxmine.api.common.utility;

import lombok.NonNull;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class BorderUtil {

    private static final ReflectionUtil reflectionUtils = new ReflectionUtil();

    public static void sendBorder(@NonNull Player player, @NonNull Location center, double size) {
        double x = center.getX();
        double z = center.getZ();
        final PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder();

        try {
            reflectionUtils.setDeclaredField(packet, "a", PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
            reflectionUtils.setDeclaredField(packet, "b", 29999984);
            reflectionUtils.setDeclaredField(packet, "c", x);
            reflectionUtils.setDeclaredField(packet, "d", z);
            reflectionUtils.setDeclaredField(packet, "e", size);
            reflectionUtils.setDeclaredField(packet, "f", size);
            reflectionUtils.setDeclaredField(packet, "g", 0L);
            reflectionUtils.setDeclaredField(packet, "h", 5);
            reflectionUtils.setDeclaredField(packet, "i",   7);
        }

        catch (IllegalAccessException | NoSuchFieldException ex2) {
            throw new RuntimeException();
        }

        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

}
