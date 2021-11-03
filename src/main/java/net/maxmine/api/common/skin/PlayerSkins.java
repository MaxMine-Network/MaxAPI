package net.maxmine.api.common.skin;

import net.maxmine.api.common.utility.MojangUtil;
import net.maxmine.api.common.utility.ReflectionUtil;
import net.maxmine.api.module.event.SkinChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class PlayerSkins {

    private static Class<?> property;

    private static SkinFactory factory;

    static {
        try {
            property = Class.forName("com.mojang.authlib.properties.Property");
        } catch (Exception e) {
            try {
                property = Class.forName("net.md_5.bungee.connection.LoginResult$Property");
            } catch (Exception ex) {
                try {
                    property = Class.forName("net.minecraft.util.com.mojang.authlib.properties.Property");
                } catch (Exception exc) {
                    System.out.println("Не удалось найти класс для установки скинов!!");
                }
            }
        }
    }

    public PlayerSkins() {
        load();
    }

    public static void applySkin(String p, String s) {
        Player player = Bukkit.getPlayerExact(p);
        if(player != null) {
            Skin skin = MojangUtil.getCachedSkinData(s);

            SkinChangeEvent skinChangeEvent = new SkinChangeEvent(player, skin);
            Bukkit.getPluginManager().callEvent(skinChangeEvent);

            if(skin != null) {
                factory.applySkin(player, createProperty(skin.getSkinName(), skin.getValue(), skin.getSignature()));
                factory.updateSkin(player);
            }
        }
    }

    private void load() {
        factory = new UniversalSkinFactory();
    }

    private static Object createProperty(String name, String value, String signature) {
        try {
            return ReflectionUtil.invokeConstructor(property,
                    new Class<?>[]{String.class, String.class, String.class}, name, value, signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
