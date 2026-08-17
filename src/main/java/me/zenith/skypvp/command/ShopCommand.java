package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.gui.ShopGui;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public ShopCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
        new ShopGui(plugin).open((Player)s);
        return true;
    }
}
