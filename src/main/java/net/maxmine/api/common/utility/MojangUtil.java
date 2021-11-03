package net.maxmine.api.common.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.maxmine.api.common.skin.Skin;
import net.maxmine.api.common.skin.SkinManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangUtil {

    private final static String UUID_URL_STRING = "https://api.minetools.eu/uuid/";
    private final static String UUID_MOJANG_URL_STRING = "https://api.mojang.com/users/profiles/minecraft/";
    private final static String UUID_BACKUP_URL_STRING = "https://api.ashcon.app/mojang/v2/user/";

    private final static String SKIN_URL_STRING = "https://api.minetools.eu/profile/";
    private final static String SKIN_MOJANG_URL_STRING = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private final static String SKIN_BACKUP_URL_STRING = "https://api.ashcon.app/mojang/v2/user/";

    private final static Gson GSON = new Gson();

    public static Skin getCachedSkinData(String name) {
        Skin skin = SkinManager.getSkinManager().getSkin(name);
        if (skin == null)
            skin = getSkinTextures(name);

        return skin;
    }

    private static String readURL(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "MaxAPI");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoOutput(true);

        String line;
        StringBuilder output = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((line = in.readLine()) != null)
            output.append(line);

        in.close();

        return output.toString();
    }

    public static String getPlayerUUID(String name) {
        try {
            String playerUUID = SkinManager.getSkinManager().getUUID(name);
            if (playerUUID == null) {
                String uuidUrl = readURL(UUID_URL_STRING + name);
                playerUUID = new JsonParser().parse(uuidUrl).getAsJsonObject().get("id").getAsString();

                SkinManager.getSkinManager().cacheUUID(name, playerUUID);
            }

            return playerUUID;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Skin getSkin(String name) throws IOException {
        String playerUUID = getPlayerUUID(name);

        String skinUrl = readURL(SKIN_URL_STRING + playerUUID + "?unsigned=false");
        JsonObject textureProperty = new JsonParser().parse(skinUrl)
                .getAsJsonObject()
                .getAsJsonObject("raw")
                .get("properties")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject();

        String texture = textureProperty.get("value").getAsString();
        String signature = textureProperty.get("signature").getAsString();

        return createSkin(name, playerUUID, texture, signature);
    }

    public static Skin createSkin(String name, String uuid, String texture, String signature) {
        Skin skin = new Skin(name, uuid, texture, signature, System.currentTimeMillis());
        String data = GSON.toJson(skin);
        SkinManager.getSkinManager().cacheSkin(name, data);

        return skin;
    }

    public static Skin getMojangSkin(String name) throws IOException {
        String playerUUID = getPlayerUUID(name);

        String skinUrl = readURL(SKIN_MOJANG_URL_STRING + playerUUID + "?unsigned=false");
        JsonObject textureProperty = new JsonParser().parse(skinUrl)
                .getAsJsonObject()
                .get("properties")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject();

        String texture = textureProperty.get("value").getAsString();
        String signature = textureProperty.get("signature").getAsString();

        return createSkin(name, playerUUID, texture, signature);
    }

    public static Skin getBackupSkin(String name) throws IOException {
        String playerUUID = getPlayerUUID(name);

        String skinUrl = readURL(SKIN_BACKUP_URL_STRING + playerUUID + "?unsigned=false");
        JsonObject textureProperty = new JsonParser().parse(skinUrl)
                .getAsJsonObject()
                .getAsJsonObject("textures")
                .get("raw")
                .getAsJsonObject();

        String texture = textureProperty.get("value").getAsString();
        String signature = textureProperty.get("signature").getAsString();

        return createSkin(name, playerUUID, texture, signature);
    }

    public static Skin getSkinTextures(String name) {
        try {
            return getSkin(name);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                return getMojangSkin(name);
            } catch (IOException ex) {
                ex.printStackTrace();
                try {
                    return getBackupSkin(name);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }

        return null;
    }


}
