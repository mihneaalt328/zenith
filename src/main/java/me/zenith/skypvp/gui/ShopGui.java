package me.zenith.skypvp.gui;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ShopGui {
    private final ZenithSkyPvP plugin;
    public ShopGui(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§b§lZENITH Shop");
        int slot = 10;
        for (String id : plugin.getShopManager().ids()) {
            inv.setItem(slot++, plugin.getShopManager().display(id));
            if (slot == 17) slot = 19;
        }
        p.openInventory(inv);
    }
}
