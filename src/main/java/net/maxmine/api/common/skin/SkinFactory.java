package net.maxmine.api.common.skin;

import net.maxmine.api.MaxAPI;
import net.maxmine.api.common.utility.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public abstract class SkinFactory {

    public void applySkin(Player p, Object props) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(MaxAPI.getInstance(), () -> {
            try {
                if (props == null)
                    return;

                Object ep = ReflectionUtil.invokeMethod(p.getClass(), p, "getHandle");
                Object profile = ReflectionUtil.invokeMethod(ep.getClass(), ep, "getProfile");
                Object propmap = ReflectionUtil.invokeMethod(profile.getClass(), profile, "getProperties");
                ReflectionUtil.invokeMethod(propmap, "clear");
                ReflectionUtil.invokeMethod(propmap.getClass(), propmap, "put", new Class[]{Object.class, Object.class}, "textures", props);
                updateSkin(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public abstract void updateSkin(Player p);

}
