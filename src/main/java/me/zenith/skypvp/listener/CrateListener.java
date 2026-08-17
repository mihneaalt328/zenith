package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CrateListener implements Listener {
    private final ZenithSkyPvP plugin;
    public CrateListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = e.getClickedBlock();
        if (b == null) return;
        String crate = plugin.getCrateManager().crateAt(b);
        if (crate == null || crate.isEmpty()) return;

        Player p = e.getPlayer();
        ItemStack hand = p.getItemInHand();
        if (hand == null || hand.getType() != Material.TRIPWIRE_HOOK || !hand.hasItemMeta() || !hand.getItemMeta().hasDisplayName()) {
            e.setCancelled(true);
            p.sendMessage("§cYou need the correct crate key.");
            return;
        }

        String expected = org.bukkit.ChatColor.stripColor(plugin.getConfig().getString("crates." + crate + ".key-name", ""));
        String actual = org.bukkit.ChatColor.stripColor(hand.getItemMeta().getDisplayName());
        if (!actual.equalsIgnoreCase(expected)) {
            e.setCancelled(true);
            p.sendMessage("§cYou need a " + expected + "§c.");
            return;
        }

        e.setCancelled(true);
        hand.setAmount(hand.getAmount() - 1);
        plugin.getCrateManager().open(p, crate);
    }
}
