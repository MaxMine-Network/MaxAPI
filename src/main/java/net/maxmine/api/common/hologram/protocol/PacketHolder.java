package net.maxmine.api.common.hologram.protocol;

import net.minecraft.server.v1_12_R1.Packet;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public interface PacketHolder {

    Packet[] getSpawnPacket();

    Packet[] getRemovePacket();

    Packet[] getUpdatePacket();

    Packet[] getVelocityPacket(double x, double y, double z);

}
