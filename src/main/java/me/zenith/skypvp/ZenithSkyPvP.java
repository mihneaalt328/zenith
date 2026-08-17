package me.zenith.skypvp;

import me.zenith.skypvp.command.*;
import me.zenith.skypvp.listener.*;
import me.zenith.skypvp.manager.*;
import me.zenith.skypvp.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ZenithSkyPvP extends JavaPlugin {
    private FileConfiguration dataConfig;
    private File dataFile;
    private FileConfiguration messages;
    private DataManager data;
    private RankHook rankHook;
    private ScoreboardManager scoreboardManager;
    private CombatManager combatManager;
    private KitManager kitManager;
    private ShopManager shopManager;
    private CrateManager crateManager;
    private TabManager tabManager;
    private AutoMessageManager autoMessageManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("data.yml", false);

        dataFile = new File(getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));

        data = new DataManager(this);
        rankHook = new RankHook(this);
        scoreboardManager = new ScoreboardManager(this);
        combatManager = new CombatManager(this);
        kitManager = new KitManager(this);
        shopManager = new ShopManager(this);
        crateManager = new CrateManager(this);
        tabManager = new TabManager(this);
        autoMessageManager = new AutoMessageManager(this);
        autoMessageManager.start();

        getCommand("zenith").setExecutor(new ZenithCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(this));
        getCommand("crates").setExecutor(new CratesCommand(this));
        getCommand("coins").setExecutor(new CoinsCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("daily").setExecutor(new DailyCommand(this));
        getCommand("top").setExecutor(new TopCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        getServer().getPluginManager().registerEvents(new CrateListener(this), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);\n        getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        long ticks = Math.max(20L, getConfig().getLong("scoreboard.update-seconds", 2) * 20L);
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
                    scoreboardManager.update(p);
                    tabManager.update(p);
                }
            }
        }, 20L, ticks);

        getLogger().info("ZenithSkyPvP 2.0.0 enabled.");
        getLogger().info("Essentials/EssentialsSpawn: spawn is delegated to Essentials.");
        getLogger().info("RankSystem hook: " + (rankHook.isHooked() ? "CONNECTED" : "NOT FOUND - using Default"));
    }

    public String msg(String key) {
        return Color.c(messages.getString(key, key).replace("{prefix}", messages.getString("prefix", "")));
    }

    public void saveDataConfig() {
        try { dataConfig.save(dataFile); }
        catch (IOException e) { getLogger().warning("Could not save data.yml: " + e.getMessage()); }
    }

    public void reloadAll() {
        reloadConfig();
        messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        data.reload();
    }

    public FileConfiguration getDataConfig() { return dataConfig; }
    public DataManager getData() { return data; }
    public RankHook getRankHook() { return rankHook; }
    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public CombatManager getCombatManager() { return combatManager; }
    public KitManager getKitManager() { return kitManager; }
    public ShopManager getShopManager() { return shopManager; }
    public CrateManager getCrateManager() { return crateManager; }
    public TabManager getTabManager() { return tabManager; }
    public AutoMessageManager getAutoMessageManager() { return autoMessageManager; }
}
