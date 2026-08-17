package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatManager {
    private final ZenithSkyPvP plugin;
    private final Map<UUID, Long> tagged = new HashMap<UUID, Long>();

    public CombatManager(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public void tag(Player p) {
        tagged.put(p.getUniqueId(), System.currentTimeMillis() + plugin.getConfig().getLong("server.combat-tag-seconds", 10) * 1000L);
    }

    public boolean inCombat(Player p) {
        Long until = tagged.get(p.getUniqueId());
        if (until == null) return false;
        if (until < System.currentTimeMillis()) {
            tagged.remove(p.getUniqueId());
            return false;
        }
        return true;
    }

    public void clear(Player p) { tagged.remove(p.getUniqueId()); }
}
