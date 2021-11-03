package net.maxmine.api.common.utility;

import net.maxmine.api.EmptyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldUtil {

    public static World getOrCreateWorld(String name) {
        World world = Bukkit.getWorld(name);
        return world == null ? Bukkit.createWorld(WorldCreator.name(name).generator(new EmptyGenerator())) : world;
    }
}
