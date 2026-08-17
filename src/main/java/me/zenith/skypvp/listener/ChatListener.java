package me.zenith.skypvp.listener;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final ZenithSkyPvP plugin;
    public ChatListener(ZenithSkyPvP plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String format = plugin.getConfig().getString("chat.format", "{prefix}&f{player} &7» &f{message}");
        format = format.replace("{prefix}", Color.c(plugin.getRankHook().getPrefix(p)))
                .replace("{player}", p.getName())
                .replace("{message}", "%2$s");
        e.setFormat(Color.c(format));
    }
}
