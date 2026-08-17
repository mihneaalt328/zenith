package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.gui.KitGui;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public KitCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
        new KitGui(plugin).open((Player)s);
        return true;
    }
}
