package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public CoinsCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 0) {
            if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
            Player p = (Player)s;
            p.sendMessage(Color.c("&8[&bZENITH&8] &fCoins: &e" + plugin.getData().coins(p.getUniqueId())));
            return true;
        }
        if (!s.hasPermission("zenith.admin")) { s.sendMessage(plugin.msg("no-permission")); return true; }
        if (a.length < 3) { s.sendMessage(Color.c("&cUsage: /coins <give|set|take> <player> <amount>")); return true; }
        Player p = plugin.getServer().getPlayer(a[1]);
        if (p == null) { s.sendMessage(Color.c("&cPlayer not found.")); return true; }
        int amount;
        try { amount = Integer.parseInt(a[2]); } catch (Exception e) { s.sendMessage(Color.c("&cAmount must be a number.")); return true; }
        if (a[0].equalsIgnoreCase("give")) plugin.getData().addCoins(p.getUniqueId(), amount);
        else if (a[0].equalsIgnoreCase("set")) plugin.getData().setCoins(p.getUniqueId(), amount);
        else if (a[0].equalsIgnoreCase("take")) plugin.getData().setCoins(p.getUniqueId(), plugin.getData().coins(p.getUniqueId()) - amount);
        else { s.sendMessage(Color.c("&cUsage: /coins <give|set|take> <player> <amount>")); return true; }
        plugin.getScoreboardManager().update(p);
        s.sendMessage(Color.c("&aCoins updated."));
        return true;
    }
}
