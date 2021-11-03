package net.maxmine.api.common.npc;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class NPCManager {

    private static final FieldAccessor ENTITY_ID = Accessors.getFieldAccessor(
            MinecraftReflection.getEntityClass(), "entityCount", true);

    private HashMap<Integer, FakePlayer> npcs = new HashMap<>();

    public void addNpc(FakePlayer player) {
        int id = (int) ENTITY_ID.get(null) - 1;
        npcs.put(id, player);
    }

    public FakePlayer getNpc(int id) {
        return npcs.get(id);
    }
}
