package net.maxmine.api.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

@AllArgsConstructor
@Getter
public enum GameType {

    UNKNOWN(Bukkit.getMotd(), "", "Hub-"),

    SWS("[§cSkyWars Solo§f] ", "§e§lSkyWars Solo", "SWLobby-"),
    SWT("[§cSkyWars Team§f] ", "§e§lSkyWars Team", "SWLobby-"),
    BWT("[§cBedWars Team§f] ", "§e§lBedWars Team", "BWLobby-"),
    EWS("[§cEggWars Solo§f] ", "§e§lEggWars Solo", "EWLobby-"),
    EWT("[§cEggWars Team§f] ", "§e§lEggWars Team", "EWLobby-"),
    CW("[§cCrazyWalls§f] ", "§e§lCrazyWalls", "CWLobby-"),
    AG("[§cArcadeGames§f] ", "§e§lArcadeGames", "AGLobby-"),
    MM("[§cMurderMystery§f] ", "§e§lMurderMystery", "MMLobby-");

    private String prefix;
    private String boardPrefix;
    private String lobby;
}
