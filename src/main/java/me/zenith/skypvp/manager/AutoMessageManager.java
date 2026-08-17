package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.Bukkit;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoMessageManager {
    private final ZenithSkyPvP plugin;
    private final AtomicInteger index = new AtomicInteger(0);
    public AutoMessageManager(ZenithSkyPvP plugin) { this.plugin = plugin; }
    public void start() {
        if (!plugin.getConfig().getBoolean("auto-messages.enabled", true)) return;
        long ticks = Math.max(20L, plugin.getConfig().getLong("auto-messages.interval-seconds", 90) * 20L);
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            List<String> list = plugin.getConfig().getStringList("auto-messages.messages");
            if (list.isEmpty()) return;
            String msg = list.get(Math.floorMod(index.getAndIncrement(), list.size()));
            Bukkit.broadcastMessage(Color.c(msg));
        }, ticks, ticks);
    }
}
