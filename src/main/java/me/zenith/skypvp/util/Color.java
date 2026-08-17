package me.zenith.skypvp.util;

import org.bukkit.ChatColor;

public final class Color {
    private Color() {}

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s == null ? "" : s);
    }

    public static String strip(String s) {
        return ChatColor.stripColor(c(s));
    }
}
