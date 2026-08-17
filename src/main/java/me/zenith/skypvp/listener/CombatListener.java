package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatListener implements Listener {
    private final ZenithSkyPvP plugin;
    public CombatListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player victim = (Player)e.getEntity();
        Player attacker = null;
        if (e.getDamager() instanceof Player) attacker = (Player)e.getDamager();
        if (attacker != null) {
            plugin.getCombatManager().tag(victim);
            plugin.getCombatManager().tag(attacker);
        }
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();

        plugin.getData().addDeath(victim.getUniqueId());
        plugin.getCombatManager().clear(victim);

        if (killer != null && killer != victim) {
            plugin.getData().addKill(killer.getUniqueId());
            int reward = plugin.getConfig().getInt("coins.kill", 50);
            plugin.getData().addCoins(killer.getUniqueId(), reward);

            int streak = plugin.getData().streak(killer.getUniqueId());
            if (streak == 5) plugin.getData().addCoins(killer.getUniqueId(), plugin.getConfig().getInt("coins.killstreak-5", 150));
            if (streak == 10) plugin.getData().addCoins(killer.getUniqueId(), plugin.getConfig().getInt("coins.killstreak-10", 300));

            killer.sendMessage("§a+§e" + reward + " coins §7for killing §f" + victim.getName() + "§7.");
            killer.sendMessage("§6Kill Streak: §f" + streak);
            plugin.getScoreboardManager().update(killer);
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getScoreboardManager().update(victim), 2L);
    }
}
