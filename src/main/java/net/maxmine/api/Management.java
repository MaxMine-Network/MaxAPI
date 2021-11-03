package net.maxmine.api;

import com.google.gson.Gson;
import lombok.Getter;
import net.maxmine.api.common.adapter.GroupAdapter;
import net.maxmine.api.common.mysql.MySQL;
import net.maxmine.api.common.npc.NPCManager;
import net.maxmine.api.common.npc.listeners.EntityListener;
import net.maxmine.api.common.skin.PlayerSkins;
import net.maxmine.api.common.skin.SkinManager;
import net.maxmine.api.common.tag.TagManager;
import net.maxmine.api.gamer.loader.GamerLoader;
import net.maxmine.api.module.kit.KitManager;

/**
 * Developed by James_TheMan, TheMrLiamt.
 * You may not change this code or change copyright.
 * The author is responsible for this code.
 */

@Getter
public class Management {

    @Getter
    private final static MySQL database = MySQL.newBuilder()
            .host("localhost")
            .user("root")
            .password("")
            .database("Groups")
            .create();

    @Getter
    private static GroupAdapter groupAdapter;

    @Getter
    private static GamerLoader gamerLoader;

    @Getter
    public final static Gson GSON = new Gson();

    @Getter
    private static NPCManager NPC;

    public Management () {
        groupAdapter = new GroupAdapter();
        gamerLoader = new GamerLoader();
        NPC = new NPCManager();

        new PlayerSkins();
        new TagManager();
        new SkinManager();
        new EntityListener();
        new KitManager();
    }
}
