package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    private final ZenithSkyPvP plugin;
    public PlayerListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!plugin.getDataConfig().contains("players." + p.getUniqueId() + ".coins")) {
            plugin.getData().setCoins(p.getUniqueId(), 0);
        }
        plugin.getScoreboardManager().update(p);
        plugin.getTabManager().update(p);
        if (!p.hasPlayedBefore()) p.sendMessage("§b§lZENITH §fWelcome to SkyPvP!");
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        plugin.getCombatManager().clear(e.getPlayer());
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getScoreboardManager().update(e.getPlayer());
            plugin.getTabManager().update(e.getPlayer());
        }, 2L);
    }
}
