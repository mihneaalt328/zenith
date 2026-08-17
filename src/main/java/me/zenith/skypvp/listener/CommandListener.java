package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.List;

public class CommandListener implements Listener {
    private final ZenithSkyPvP plugin;
    public CommandListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void command(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getCombatManager().inCombat(p) || p.hasPermission("zenith.bypass")) return;
        if (!plugin.getConfig().getBoolean("combat.block-commands", true)) return;

        String command = e.getMessage().split(" ")[0].toLowerCase();
        List<String> allowed = plugin.getConfig().getStringList("combat.allowed-commands");
        for (String a : allowed) {
            if (command.equalsIgnoreCase(a)) return;
        }
        e.setCancelled(true);
        p.sendMessage(plugin.msg("combat"));
    }
}
