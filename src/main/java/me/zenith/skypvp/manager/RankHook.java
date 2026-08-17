package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;
import java.lang.reflect.Method;
import java.util.UUID;

public class RankHook {
    private final ZenithSkyPvP plugin;
    private final boolean hooked;

    public RankHook(ZenithSkyPvP plugin) {
        this.plugin = plugin;
        this.hooked = plugin.getServer().getPluginManager().getPlugin("RankSystem") != null;
    }

    public boolean isHooked() { return hooked; }

    public String getRank(Player player) {
        Object value = invoke("getRank", player.getUniqueId());
        if (value == null) value = invoke("getRank", player.getName());
        if (value == null) return "Default";
        return String.valueOf(value);
    }

    public String getPrefix(Player player) {
        Object value = invoke("getPrefix", player.getUniqueId());
        if (value == null) value = invoke("getPrefix", player.getName());
        if (value == null) {
            // If RankSystem only exposes a rank, provide a readable fallback prefix.
            String rank = getRank(player);
            if (!rank.equalsIgnoreCase("Default")) return "&b[" + rank + "] ";
            return "&7[Player] ";
        }
        return String.valueOf(value);
    }

    private Object invoke(String methodName, Object arg) {
        if (!hooked) return null;
        try {
            Object rs = plugin.getServer().getPluginManager().getPlugin("RankSystem");
            // Try direct plugin methods first.
            for (Method m : rs.getClass().getMethods()) {
                if (!m.getName().equalsIgnoreCase(methodName) || m.getParameterTypes().length != 1) continue;
                Class<?> t = m.getParameterTypes()[0];
                if (!t.isAssignableFrom(arg.getClass()) && !(t == String.class && arg instanceof UUID)) continue;
                return m.invoke(rs, (t == String.class && arg instanceof UUID) ? arg.toString() : arg);
            }

            // Try common manager getter patterns.
            for (String getter : new String[]{"getRankManager", "getPlayerRankManager"}) {
                try {
                    Method gm = rs.getClass().getMethod(getter);
                    Object manager = gm.invoke(rs);
                    for (Method m : manager.getClass().getMethods()) {
                        if (!m.getName().equalsIgnoreCase(methodName) || m.getParameterTypes().length != 1) continue;
                        Class<?> t = m.getParameterTypes()[0];
                        if (!t.isAssignableFrom(arg.getClass()) && !(t == String.class && arg instanceof UUID)) continue;
                        return m.invoke(manager, (t == String.class && arg instanceof UUID) ? arg.toString() : arg);
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return null;
    }
}
