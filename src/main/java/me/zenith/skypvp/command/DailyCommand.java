package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class DailyCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public DailyCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
        Player p = (Player) s;
        long now = System.currentTimeMillis();
        long next = plugin.getData().daily(p.getUniqueId());
        if (next > now && !p.hasPermission("zenith.bypass")) {
            p.sendMessage(plugin.msg("daily-cooldown").replace("{time}", format((next-now)/1000)));
            return true;
        }
        int amount = plugin.getConfig().getInt("coins.daily", 500);
        plugin.getData().addCoins(p.getUniqueId(), amount);
        plugin.getData().setDaily(p.getUniqueId(), now + plugin.getConfig().getLong("daily.cooldown-seconds", 86400)*1000L);
        p.sendMessage(plugin.msg("daily-claimed").replace("{coins}", String.valueOf(amount)));
        plugin.getScoreboardManager().update(p);
        return true;
    }

    private String format(long sec) {
        long h=sec/3600; sec%=3600;
        long m=sec/60; sec%=60;
        if(h>0) return h+"h "+m+"m";
        if(m>0) return m+"m "+sec+"s";
        return sec+"s";
    }
}
