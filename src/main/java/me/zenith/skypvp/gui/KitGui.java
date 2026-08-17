package me.zenith.skypvp.gui;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitGui {
    private final ZenithSkyPvP plugin;
    public KitGui(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§b§lZENITH Kits");
        int slot = 10;
        for (String kit : plugin.getConfig().getConfigurationSection("kits").getKeys(false)) {
            String permission = plugin.getConfig().getString("kits." + kit + ".permission", "");
            boolean has = permission.isEmpty() || p.hasPermission(permission);
            Material mat = kit.equalsIgnoreCase("MVP") ? Material.DIAMOND_CHESTPLATE :
                    kit.equalsIgnoreCase("VIP") ? Material.GOLD_CHESTPLATE : Material.IRON_CHESTPLATE;
            inv.setItem(slot++, ItemUtil.item(mat, 1, has ? "&b" + kit : "&c" + kit,
                    has ? "&aClick to claim." : "&cLocked.",
                    "&7Cooldown: &f" + plugin.getConfig().getLong("kits." + kit + ".cooldown") + "s"));
            if (slot == 17) slot = 19;
        }
        p.openInventory(inv);
    }
}
