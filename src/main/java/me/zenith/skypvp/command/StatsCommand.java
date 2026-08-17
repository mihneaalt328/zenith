package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public StatsCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        Player p;
        if (a.length == 0) {
            if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
            p = (Player)s;
        } else {
            p = plugin.getServer().getPlayer(a[0]);
            if (p == null) { s.sendMessage(Color.c("&cPlayer not found or offline.")); return true; }
        }
        s.sendMessage(plugin.msg("stats"));
        s.sendMessage(Color.c("&bPlayer: &f" + p.getName()));
        s.sendMessage(Color.c("&7Kills: &a" + plugin.getData().kills(p.getUniqueId())));
        s.sendMessage(Color.c("&7Deaths: &c" + plugin.getData().deaths(p.getUniqueId())));
        s.sendMessage(Color.c("&7Current streak: &6" + plugin.getData().streak(p.getUniqueId())));
        s.sendMessage(Color.c("&7Best streak: &6" + plugin.getData().bestStreak(p.getUniqueId())));
        s.sendMessage(Color.c("&7Coins: &e" + plugin.getData().coins(p.getUniqueId())));
        s.sendMessage(plugin.msg("stats-end"));
        return true;
    }
}
