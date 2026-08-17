package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.gui.CrateGui;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CratesCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public CratesCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
        new CrateGui(plugin).open((Player)s);
        return true;
    }
}
