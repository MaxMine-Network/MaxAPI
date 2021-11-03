package net.maxmine.api.game.selector;

import net.maxmine.api.EmptyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */
public class GameMapSelector {

    public World selectMap() {
        List<String> maps = this.getAllMaps();
        String name = Bukkit.getServerName();

        int i = name.lastIndexOf(45);
        int index;

        try {
            index = Integer.parseInt(name.substring(i + 1));
        } catch (Exception e) {
            throw new IllegalStateException("Invalid server name " + Bukkit.getServerName());
        }

        int mapIndex = index % maps.size();
        String map = maps.get(mapIndex);

        World world = Bukkit.createWorld(new WorldCreator(map).generator(new EmptyGenerator()));
        System.out.println("[GameMapSelector] Selected world #" + mapIndex + "/" + maps.size() + " '" + map + "'");
        return world;
    }

    private List<String> getAllMaps() {
        File mapDir = Bukkit.getWorldContainer();

        if (!mapDir.exists() || !mapDir.isDirectory()) {
            throw new IllegalStateException(mapDir.getAbsolutePath() + " is not a directory");
        }

        File[] files = mapDir.listFiles();

        if (files == null) {
            throw new IllegalStateException("No files in " + mapDir.getAbsolutePath());
        }

        List<String> maps = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory() && new File(file, "region").isDirectory()) {
                maps.add(file.getName());
            }
        }

        this.filterMaps(maps);

        if (maps.isEmpty()) {
            throw new IllegalStateException("No valid maps found in " + mapDir.getAbsolutePath());
        }
        return maps;
    }

    private void filterMaps(List<String> maps) {
        maps.removeIf(s -> s.toLowerCase().startsWith("lobby"));
    }

}
