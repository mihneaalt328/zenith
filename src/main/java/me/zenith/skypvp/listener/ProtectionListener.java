package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProtectionListener implements Listener {
    private final ZenithSkyPvP plugin;
    public ProtectionListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    private boolean protectedArea(Player p) {
        if (p.hasPermission("zenith.bypass")) return false;
        org.bukkit.Location spawn = p.getWorld().getSpawnLocation();
        return p.getLocation().distanceSquared(spawn) <= Math.pow(plugin.getConfig().getDouble("server.protect-spawn-radius", 18), 2);
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        if (protectedArea(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        if (protectedArea(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getDamager() instanceof Player && protectedArea((Player)e.getEntity())) e.setCancelled(true);
    }
}
