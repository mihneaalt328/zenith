package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataManager {
    private final ZenithSkyPvP plugin;
    private final File file;
    private FileConfiguration data;

    public DataManager(ZenithSkyPvP plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    private String base(UUID uuid) { return "players." + uuid; }

    public int coins(UUID uuid) { return data.getInt(base(uuid) + ".coins", 0); }
    public void setCoins(UUID uuid, int value) { data.set(base(uuid) + ".coins", Math.max(0, value)); save(); }
    public void addCoins(UUID uuid, int value) { setCoins(uuid, coins(uuid) + value); }

    public int kills(UUID uuid) { return data.getInt(base(uuid) + ".kills", 0); }
    public int deaths(UUID uuid) { return data.getInt(base(uuid) + ".deaths", 0); }
    public int streak(UUID uuid) { return data.getInt(base(uuid) + ".streak", 0); }
    public int bestStreak(UUID uuid) { return data.getInt(base(uuid) + ".best-streak", 0); }

    public void addKill(UUID uuid) {
        int s = streak(uuid) + 1;
        data.set(base(uuid) + ".kills", kills(uuid) + 1);
        data.set(base(uuid) + ".streak", s);
        data.set(base(uuid) + ".best-streak", Math.max(bestStreak(uuid), s));
        save();
    }

    public void addDeath(UUID uuid) {
        data.set(base(uuid) + ".deaths", deaths(uuid) + 1);
        data.set(base(uuid) + ".streak", 0);
        save();
    }

    public long cooldown(UUID uuid, String kit) {
        return data.getLong(base(uuid) + ".cooldowns." + kit, 0L);
    }

    public void setCooldown(UUID uuid, String kit, long timestamp) {
        data.set(base(uuid) + ".cooldowns." + kit, timestamp);
        save();
    }

    public void setDaily(UUID uuid, long timestamp) {
        data.set(base(uuid) + ".daily", timestamp);
        save();
    }

    public long daily(UUID uuid) {
        return data.getLong(base(uuid) + ".daily", 0L);
    }

    public void save() {
        try { data.save(file); }
        catch (IOException e) { plugin.getLogger().severe("Could not save data.yml: " + e.getMessage()); }
    }

    public void reload() { data = YamlConfiguration.loadConfiguration(file); }
}
