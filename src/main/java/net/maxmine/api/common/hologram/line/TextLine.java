package net.maxmine.api.common.hologram.line;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.maxmine.api.common.hologram.Hologram;
import net.maxmine.api.common.hologram.HologramLine;
import net.maxmine.api.common.hologram.protocol.PacketHolder;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

public class TextLine implements HologramLine, PacketHolder {

    private Hologram hologram;
    private String text;
    @Getter
    private Location location;

    @Getter
    private EntityArmorStand stand;

    public TextLine(String text) {
            this.text = text;
        }

        @Override
        public void setOwningHologram(Hologram hologram) {
            Preconditions.checkArgument(this.hologram == null, "Owning hologram already set!");
            this.hologram = hologram;
        }

        @Override
        public Hologram getOwningHologram() {
            return hologram;
        }

        @Override
        public void create(Location loc) {
            stand = new EntityArmorStand(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
            stand.setInvisible(true);
            stand.setCustomNameVisible(true);
            this.location = loc;
            updateText();
        }

        public void setHead(ItemStack item) {
            stand.setEquipment(EnumItemSlot.HEAD, item);
        }

        @Override
        public void modify(String text) {
            this.text = text;
            updateText();
        }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        stand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        hologram.removeLine(this);
        hologram.insertLine(this);
    }

    @Override
        public Packet[] getSpawnPacket() {
            return new Packet[] {
                    new PacketPlayOutSpawnEntityLiving(stand)
            };
        }

        @Override
        public Packet[] getRemovePacket() {
            return new Packet[] {
                    new PacketPlayOutEntityDestroy(stand.getId())
            };
        }

        @Override
        public Packet[] getUpdatePacket() {
            return new Packet[] {
                    new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true)
            };
        }

    @Override
    public Packet[] getVelocityPacket(double x, double y, double z) {
        return new Packet[]{
                new PacketPlayOutEntityVelocity(stand.getId(), x, y, z)
        };
    }

    public void updateText() {
            stand.setCustomName(text);
            hologram.getVisibilityManager().update();
        }
    }

