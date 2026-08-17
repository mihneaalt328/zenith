package me.zenith.skypvp.command;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final ZenithSkyPvP plugin;
    public SpawnCommand(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) { s.sendMessage(plugin.msg("player-only")); return true; }
        Player p = (Player)s;
        if (plugin.getCombatManager().inCombat(p) && !p.hasPermission("zenith.bypass")) {
            p.sendMessage(plugin.msg("combat")); return true;
        }
        String world = plugin.getDataConfig().getString("spawn.world", plugin.getConfig().getString("server.spawn-world", "world"));
        org.bukkit.World w = plugin.getServer().getWorld(world);
        if (w == null) { p.sendMessage("§cSpawn world not found."); return true; }
        Location loc = new Location(w,
                plugin.getDataConfig().getDouble("spawn.x", 0.5),
                plugin.getDataConfig().getDouble("spawn.y", 100),
                plugin.getDataConfig().getDouble("spawn.z", 0.5),
                (float)plugin.getDataConfig().getDouble("spawn.yaw", 0),
                (float)plugin.getDataConfig().getDouble("spawn.pitch", 0));
        p.teleport(loc);
        p.sendMessage(plugin.msg("spawn"));
        return true;
    }
}
