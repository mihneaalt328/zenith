package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    private final ZenithSkyPvP plugin;

    public ShopManager(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public List<String> ids() {
        return new ArrayList<String>(plugin.getConfig().getConfigurationSection("shop.items").getKeys(false));
    }

    public ItemStack display(String id) {
        String path = "shop.items." + id;
        Material m = Material.valueOf(plugin.getConfig().getString(path + ".material"));
        int amount = plugin.getConfig().getInt(path + ".amount", 1);
        int price = plugin.getConfig().getInt(path + ".price", 0);
        String name = plugin.getConfig().getString(path + ".name", id);
        return ItemUtil.item(m, amount, name, "&7Price: &e" + price + " coins", "&aClick to purchase");
    }

    public void buy(Player p, String id) {
        String path = "shop.items." + id;
        if (!plugin.getConfig().isConfigurationSection(path)) return;
        int price = plugin.getConfig().getInt(path + ".price", 0);
        if (plugin.getData().coins(p.getUniqueId()) < price) {
            p.sendMessage(plugin.msg("not-enough-coins").replace("{price}", String.valueOf(price)));
            return;
        }
        Material m = Material.valueOf(plugin.getConfig().getString(path + ".material"));
        int amount = plugin.getConfig().getInt(path + ".amount", 1);
        String name = plugin.getConfig().getString(path + ".name", id);
        plugin.getData().setCoins(p.getUniqueId(), plugin.getData().coins(p.getUniqueId()) - price);
        p.getInventory().addItem(new ItemStack(m, amount));
        p.sendMessage(plugin.msg("purchase")
                .replace("{item}", name)
                .replace("{price}", String.valueOf(price)));
        plugin.getScoreboardManager().update(p);
    }
}
