package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
    private final ZenithSkyPvP plugin;
    public GuiListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player)e.getWhoClicked();
        String title = e.getView().getTitle();
        if (title == null) return;

        if (title.contains("ZENITH Kits")) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
            String name = org.bukkit.ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (plugin.getConfig().isConfigurationSection("kits." + name)) {
                p.closeInventory();
                plugin.getKitManager().give(p, name);
            }
        } else if (title.contains("ZENITH Shop")) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
            String display = item.getItemMeta().getDisplayName();
            for (String id : plugin.getShopManager().ids()) {
                if (display.equals(plugin.getShopManager().display(id).getItemMeta().getDisplayName())) {
                    plugin.getShopManager().buy(p, id);
                    return;
                }
            }
        } else if (title.contains("ZENITH Crates")) {
            e.setCancelled(true);
        }
    }
}
