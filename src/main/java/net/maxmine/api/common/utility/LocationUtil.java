package net.maxmine.api.common.utility;

import net.maxmine.api.EmptyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class LocationUtil {

    public static Location stringToNewLocation(String line) {
        if (line == null) {
            return null;
        }
        String[] loc = line.split(";");
        World world = WorldUtil.getOrCreateWorld(loc[0]);

        Location location = new Location(world, Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
        System.out.println("ЗАГРУЖЕНА ЛОКАЦИЯ - " + location.toString());

        if (loc.length > 4) {
            location.setPitch(Float.parseFloat(loc[4]));
            location.setYaw(Float.parseFloat(loc[5]));
        }
        return location;
    }

    public static Location stringToLoc(String location) {
        String[] args = location.split(";");
        World world = getOrCreate(args[0]);
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        if(args.length > 4) {
            float yaw = Float.parseFloat(args[4]);
            float pitch = Float.parseFloat(args[5]);
            return new Location(world, x, y, z, yaw, pitch);
        }
        return new Location(world, x, y, z);
    }

    private static World getOrCreate(String name) {
        World world = Bukkit.getWorld(name);

        return world == null ? Bukkit.createWorld(WorldCreator.name(name).generator(new EmptyGenerator())) : world;
    }

    public static String locationToString(Location loc) {
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        return world + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
    }

    public static List<Location> stringListToLoc(List<String> loc) {
        List<Location> locations = new ArrayList<>();
        loc.forEach(l -> locations.add(stringToLoc(l)));
        return locations;
    }

    public static Player getFirstNearPlayer(Location location, double raduis) {
        Collection<Entity> near = location.getWorld().getNearbyEntities(location, raduis, raduis, raduis);

        raduis *= raduis;

        for (Entity entity : near) {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                if (player.getLocation().distanceSquared(location) < raduis) {
                    return player;
                }
            }
        }
        return null;
    }

}
