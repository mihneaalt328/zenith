package me.zenith.skypvp.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class ItemUtil {
    private ItemUtil() {}

    public static ItemStack item(Material material, int amount, String name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (name != null) meta.setDisplayName(Color.c(name));
        if (lore != null && lore.length > 0) {
            String[] colored = new String[lore.length];
            for (int i = 0; i < lore.length; i++) colored[i] = Color.c(lore[i]);
            meta.setLore(Arrays.asList(colored));
        }
        item.setItemMeta(meta);
        return item;
    }
}
