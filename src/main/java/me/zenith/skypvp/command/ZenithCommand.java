package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.command.*;

public class ZenithCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public ZenithCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public boolean onCommand(CommandSender s, Command c, String label, String[] a) {
        if (a.length == 0 || a[0].equalsIgnoreCase("help")) {
            s.sendMessage(Color.c("&8&m--------------------------------"));
            s.sendMessage(Color.c("&b&lZENITH SkyPvP &7- Commands"));
            s.sendMessage(Color.c("&e/zenith help &7- Show help"));
            s.sendMessage(Color.c("&e/zenith reload &7- Reload configuration"));

            s.sendMessage(Color.c("&e/zenith setcrate <name> &7- Set crate at target block"));
            s.sendMessage(Color.c("&e/zenith givekey <player> <crate> [amount]"));

            s.sendMessage(Color.c("&e/spawn &7- EssentialsX spawn"));
            s.sendMessage(Color.c("&e/kit &7- Open kits"));
            s.sendMessage(Color.c("&e/shop &7- Open shop"));
            s.sendMessage(Color.c("&e/crates &7- Open crates"));
            s.sendMessage(Color.c("&e/coins &7- Check coins"));
            s.sendMessage(Color.c("&e/stats &7- Check stats"));
            s.sendMessage(Color.c("&8&m--------------------------------"));
            return true;
        }
        if (!s.hasPermission("zenith.admin")) {
            s.sendMessage(plugin.msg("no-permission"));
            return true;
        }

        if (a[0].equalsIgnoreCase("reload")) {
            plugin.reloadAll();
            s.sendMessage(plugin.msg("reload"));
            return true;
        }
        if (a[0].equalsIgnoreCase("setcrate")) {
            if (!(s instanceof org.bukkit.entity.Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
            if (a.length < 2) { s.sendMessage(Color.c("&cUsage: /zenith setcrate <name>")); return true; }
            org.bukkit.entity.Player p = (org.bukkit.entity.Player)s;
            org.bukkit.block.Block b = p.getTargetBlock(null, 6);
            if (b == null) { s.sendMessage(Color.c("&cLook at a block.")); return true; }
            if (!plugin.getConfig().isConfigurationSection("crates." + a[1])) { s.sendMessage(Color.c("&cUnknown crate.")); return true; }
            plugin.getCrateManager().setCrate(b, a[1]);
            s.sendMessage(Color.c("&aSet &f" + a[1] + " &acrate."));
            return true;
        }
        if (a[0].equalsIgnoreCase("givekey")) {
            if (a.length < 3) { s.sendMessage(Color.c("&cUsage: /zenith givekey <player> <crate> [amount]")); return true; }
            org.bukkit.entity.Player p = plugin.getServer().getPlayer(a[1]);
            if (p == null) { s.sendMessage(Color.c("&cPlayer not found.")); return true; }
            int amount = a.length >= 4 ? Integer.parseInt(a[3]) : 1;
            plugin.getCrateManager().giveKey(p, a[2], amount);
            return true;
        }
        s.sendMessage(plugin.msg("unknown-command"));
        return true;
    }

}
