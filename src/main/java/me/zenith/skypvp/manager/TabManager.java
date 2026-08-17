package me.zenith.skypvp.manager;

import me.zenith.skypvp.ZenithSkyPvP;
import me.zenith.skypvp.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class TabManager {
    private final ZenithSkyPvP plugin;
    public TabManager(ZenithSkyPvP plugin) { this.plugin = plugin; }

    public void update(Player viewer) {
        if (!plugin.getConfig().getBoolean("tab.enabled", true)) return;
        Scoreboard board = viewer.getScoreboard();
        if (board == null) return;
        for (Player target : plugin.getServer().getOnlinePlayers()) {
            String name = target.getName();
            if (name.length() > 16) continue;
            String teamName = teamName(target);
            Team team = board.getTeam(teamName);
            if (team == null) team = board.registerNewTeam(teamName);
            if (plugin.getConfig().getBoolean("tab.team-prefix-enabled", true)) {
                String prefix = Color.c(plugin.getRankHook().getPrefix(target));
                if (prefix.length() > 16) prefix = prefix.substring(0, 16);
                team.setPrefix(prefix);
            }
            for (Team t : board.getTeams()) {
                if (t.getName().startsWith("z") && t.hasEntry(name) && !t.getName().equals(teamName)) t.removeEntry(name);
            }
            if (!team.hasEntry(name)) team.addEntry(name);
        }
        sendHeaderFooter(viewer);
    }

    private void sendHeaderFooter(Player p) {
        try {
            String header = replace(join("tab.header"));
            String footer = replace(join("tab.footer"));
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle").invoke(p);
            Field connection = handle.getClass().getField("playerConnection");
            Object pc = connection.get(handle);
            Class<?> packetClass = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter");
            Object packet = packetClass.newInstance();
            Class<?> serializer = Class.forName("net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer");
            Method parse = serializer.getMethod("a", String.class);
            Object h = parse.invoke(null, "{"text":"" + jsonEscape(header) + ""}");
            Object f = parse.invoke(null, "{"text":"" + jsonEscape(footer) + ""}");
            Field a = packetClass.getDeclaredField("a"), b = packetClass.getDeclaredField("b");
            a.setAccessible(true); b.setAccessible(true); a.set(packet, h); b.set(packet, f);
            Method send = pc.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server.v1_8_R3.Packet"));
            send.invoke(pc, packet);
        } catch (Throwable ignored) {}
    }

    private String join(String path) {
        List<String> lines = plugin.getConfig().getStringList(path);
        StringBuilder b = new StringBuilder();
        for (String line : lines) b.append(line).append("\n");
        return b.toString().trim();
    }
    private String replace(String s) {
        return Color.c(s.replace("{online}", String.valueOf(plugin.getServer().getOnlinePlayers().size()))
                .replace("{max}", String.valueOf(plugin.getServer().getMaxPlayers()))
                .replace("{ip}", plugin.getConfig().getString("server.ip", "skypvp.zenithmc.net")));
    }
    private String jsonEscape(String s) {
        return s.replace("\", "\\").replace(""", "\"").replace("
", "\n");
    }
    private String teamName(Player p) {
        String rank = plugin.getRankHook().getRank(p).replaceAll("[^A-Za-z0-9]", "");
        if (rank.length() > 8) rank = rank.substring(0, 8);
        String id = Integer.toHexString(p.getUniqueId().hashCode());
        String n = "z" + rank + id;
        return n.substring(0, Math.min(16, n.length()));
    }
}
