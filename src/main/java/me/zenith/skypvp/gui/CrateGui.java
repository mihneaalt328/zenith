package me.zenith.skypvp.gui;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CrateGui {
    private final ZenithSkyPvP plugin;
    public CrateGui(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lZENITH Crates");
        int slot = 11;
        for (String crate : plugin.getConfig().getConfigurationSection("crates").getKeys(false)) {
            inv.setItem(slot++, ItemUtil.item(Material.CHEST, 1, plugin.getConfig().getString("crates." + crate + ".key-name"),
                    "&7Find this crate in the map.", "&eRight-click the crate to open."));
        }
        p.openInventory(inv);
    }
}
