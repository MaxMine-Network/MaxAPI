package net.maxmine.api.game;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Team {

    @Getter
    private static Team team;

    public Team() {
        team = this;
    }

    public abstract void mergePlayersInTeams(List<Player> playerList);
}
