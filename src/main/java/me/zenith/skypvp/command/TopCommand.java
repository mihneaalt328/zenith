package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.command.*;
import java.util.*;

public class TopCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public TopCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        String type = a.length == 0 ? "kills" : a[0].toLowerCase();
        if (!type.equals("kills") && !type.equals("coins") && !type.equals("streak")) type = "kills";

        List<UUID> ids = new ArrayList<UUID>();
        if (plugin.getDataConfig().isConfigurationSection("players")) {
            for (String id : plugin.getDataConfig().getConfigurationSection("players").getKeys(false)) {
                try { ids.add(UUID.fromString(id)); } catch (Exception ignored) {}
            }
        }

        final String metric = type;
        Collections.sort(ids, new Comparator<UUID>() {
            public int compare(UUID a, UUID b) {
                int va = value(a, metric), vb = value(b, metric);
                return Integer.compare(vb, va);
            }
            private int value(UUID id, String m) {
                if (m.equals("coins")) return plugin.getData().coins(id);
                if (m.equals("streak")) return plugin.getData().bestStreak(id);
                return plugin.getData().kills(id);
            }
        });

        s.sendMessage(Color.c("&8&m--------------------------------"));
        s.sendMessage(Color.c("&b&lZENITH &fTOP &7- &e" + metric.toUpperCase()));
        int shown=0;
        for (UUID id : ids) {
            if (shown >= 10) break;
            String name = plugin.getServer().getOfflinePlayer(id).getName();
            int value = metric.equals("coins") ? plugin.getData().coins(id) :
                    metric.equals("streak") ? plugin.getData().bestStreak(id) : plugin.getData().kills(id);
            shown++;
            s.sendMessage(Color.c("&e#" + shown + " &f" + name + " &7» &b" + value));
        }
        if (shown == 0) s.sendMessage(Color.c("&7No stats yet."));
        s.sendMessage(Color.c("&8&m--------------------------------"));
        return true;
    }
}
