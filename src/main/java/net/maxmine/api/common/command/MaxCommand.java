package net.maxmine.api.common.command;

import net.maxmine.api.Management;
import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.adapter.GroupAdapter;
import net.maxmine.api.gamer.Gamer;
import net.maxmine.api.gamer.loader.GamerLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Developed by James_TheMan.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@SuppressWarnings("all")
public abstract class MaxCommand extends Command implements CommandExecutor {

    private final GamerLoader gamerLoader = Management.getGamerLoader();

    private static CommandMap commandMap;

    private int minLevel;

    static {
        try {
            String version = MaxAPI.getPlugin(MaxAPI.class).getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
            Object craftServerObject = craftServerClass.cast((Object) MaxAPI.getPlugin(MaxAPI.class).getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap)commandMapField.get(craftServerObject);
        } catch (Exception ignore) {}
    }

    private MaxCommand(String command, List<String> aliases) {
        super(command, "", "", aliases);

        commandMap.register(MaxAPI.getPlugin(MaxAPI.class).getDescription().getName(), this);
    }

    public MaxCommand(String command) {
        this(command, new ArrayList<>());
    }

    public MaxCommand(String command, int minLevel) {
        this(command, new ArrayList<>());

        this.minLevel = minLevel;
    }

    public MaxCommand(String command, int minLevel, String... aliases) {
        this(command, Arrays.asList(aliases));

        this.minLevel = minLevel;
    }

    public abstract void execute(Player player, Gamer gamer, String[] args);

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(!(commandSender instanceof Player)) return true;

        Gamer gamer = gamerLoader.getGamer(commandSender.getName());
        Player player = Bukkit.getPlayer(commandSender.getName());

        GroupAdapter groupAdapter = Management.getGroupAdapter();

        if(gamer.getGroup().getLevel() < minLevel) {
            commandSender.sendMessage(String.format("§f[§cMaxMine§f] Вам необходима привилегия %s§f для выполнения команды", groupAdapter.getGroupByLevel(minLevel)));
            return true;
        }

        execute(player, gamer, args);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        return true;
    }
}
