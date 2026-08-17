package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import me.zenith.skypvp.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CrateManager {
    private final ZenithSkyPvP plugin;
    private final Random random = new Random();

    public CrateManager(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public ItemStack key(String crate) {
        String name = plugin.getConfig().getString("crates." + crate + ".key-name", "&e" + crate + " Key");
        return ItemUtil.item(Material.TRIPWIRE_HOOK, 1, name, "&7Right-click the matching crate.");
    }

    public void giveKey(Player p, String crate, int amount) {
        for (int i = 0; i < amount; i++) p.getInventory().addItem(key(crate));
        p.sendMessage(plugin.msg("crate-key").replace("{crate}", crate));
    }

    public boolean isCrate(Block b, String crate) {
        String loc = location(b);
        return plugin.getDataConfig().getString("crates." + loc, "").equalsIgnoreCase(crate);
    }

    public String crateAt(Block b) {
        return plugin.getDataConfig().getString("crates." + location(b), "");
    }

    public void setCrate(Block b, String crate) {
        plugin.getDataConfig().set("crates." + location(b), crate);
        plugin.getData().save();
    }

    public void open(Player p, String crate) {
        List<String> rewards = plugin.getConfig().getStringList("crates." + crate + ".rewards");
        if (rewards.isEmpty()) return;

        int total = 0;
        for (String r : rewards) {
            String[] x = r.split(":");
            if (x.length >= 3) total += Integer.parseInt(x[2]);
        }
        int roll = random.nextInt(Math.max(total, 1)) + 1;
        String chosen = rewards.get(0);
        int current = 0;
        for (String r : rewards) {
            String[] x = r.split(":");
            if (x.length < 3) continue;
            current += Integer.parseInt(x[2]);
            if (roll <= current) { chosen = r; break; }
        }

        giveReward(p, chosen);
    }

    private void giveReward(Player p, String reward) {
        String[] x = reward.split(":");
        String type = x[0];
        int amount = x.length > 1 ? Integer.parseInt(x[1]) : 1;

        if (type.equalsIgnoreCase("COINS")) {
            plugin.getData().addCoins(p.getUniqueId(), amount);
            p.sendMessage(plugin.msg("crate-win").replace("{reward}", amount + " Coins"));
        } else {
            try {
                Material m = Material.valueOf(type.toUpperCase());
                p.getInventory().addItem(new ItemStack(m, amount));
                p.sendMessage(plugin.msg("crate-win").replace("{reward}", amount + "x " + m.name()));
            } catch (Exception e) {
                p.sendMessage(Color.c("&cInvalid crate reward: " + reward));
            }
        }
        plugin.getScoreboardManager().update(p);
    }

    private String location(Block b) {
        return b.getWorld().getName() + "," + b.getX() + "," + b.getY() + "," + b.getZ();
    }
}
