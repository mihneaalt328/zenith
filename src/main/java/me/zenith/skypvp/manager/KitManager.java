package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitManager {
    private final ZenithSkyPvP plugin;

    public KitManager(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public boolean give(Player p, String kit) {
        String path = "kits." + kit;
        if (!plugin.getConfig().isConfigurationSection(path)) return false;

        String permission = plugin.getConfig().getString(path + ".permission", "");
        if (!permission.isEmpty() && !p.hasPermission(permission)) {
            p.sendMessage(plugin.msg("kit-no-permission"));
            return true;
        }

        long now = System.currentTimeMillis();
        long cd = plugin.getConfig().getLong(path + ".cooldown", 86400) * 1000L;
        long next = plugin.getData().cooldown(p.getUniqueId(), kit);
        if (next > now && !p.hasPermission("zenith.bypass")) {
            p.sendMessage(plugin.msg("kit-cooldown").replace("{time}", format((next - now) / 1000)));
            return true;
        }

        for (String raw : plugin.getConfig().getStringList(path + ".items")) {
            String[] parts = raw.split(":");
            try {
                Material mat = Material.valueOf(parts[0].toUpperCase());
                int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
                p.getInventory().addItem(new ItemStack(mat, amount));
            } catch (Exception ignored) {}
        }

        plugin.getData().setCooldown(p.getUniqueId(), kit, now + cd);
        p.sendMessage(plugin.msg("kit-given").replace("{kit}", kit));
        plugin.getScoreboardManager().update(p);
        return true;
    }

    private String format(long sec) {
        long h = sec / 3600; sec %= 3600;
        long m = sec / 60; sec %= 60;
        if (h > 0) return h + "h " + m + "m";
        if (m > 0) return m + "m " + sec + "s";
        return sec + "s";
    }
}
