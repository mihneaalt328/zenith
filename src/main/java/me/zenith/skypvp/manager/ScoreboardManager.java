package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {
    private final ZenithSkyPvP plugin;

    public ScoreboardManager(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public void update(Player p) {
        if (!plugin.getConfig().getBoolean("scoreboard.enabled", true)) return;

        org.bukkit.scoreboard.Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        String title = Color.c(plugin.getConfig().getString("scoreboard.title", "&b&lZENITH"));
        Objective obj = board.registerNewObjective("zenith", "dummy");
        obj.setDisplayName(title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        String rank = plugin.getRankHook().getRank(p);
        String[] lines = plugin.getConfig().getStringList("scoreboard.lines").toArray(new String[0]);
        int score = lines.length;
        for (String raw : lines) {
            String line = raw
                    .replace("{rank}", rank)
                    .replace("{coins}", String.valueOf(plugin.getData().coins(p.getUniqueId())))
                    .replace("{kills}", String.valueOf(plugin.getData().kills(p.getUniqueId())))
                    .replace("{deaths}", String.valueOf(plugin.getData().deaths(p.getUniqueId())))
                    .replace("{streak}", String.valueOf(plugin.getData().streak(p.getUniqueId())))
                    .replace("{best}", String.valueOf(plugin.getData().bestStreak(p.getUniqueId())))
                    .replace("{online}", String.valueOf(plugin.getServer().getOnlinePlayers().size()))
                    .replace("{max}", String.valueOf(plugin.getServer().getMaxPlayers()));
            // Make duplicate blank lines unique for 1.8 scoreboards.
            if (line.trim().isEmpty()) line = " ";
            line = Color.c(line);
            while (hasEntry(board, line)) line = line + "§r";
            obj.getScore(line).setScore(score--);
        }
        p.setScoreboard(board);
    }

    private boolean hasEntry(Scoreboard b, String s) {
        for (String e : b.getEntries()) if (e.equals(s)) return true;
        return false;
    }
}
