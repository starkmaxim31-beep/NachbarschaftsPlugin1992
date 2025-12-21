package de.nachbarschaft;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class ChapterManager {

    private final Main plugin;

    public ChapterManager(Main plugin) {
        this.plugin = plugin;
    }

    public int getChapter(Player p) {
        return plugin.getConfig().getInt("players." + p.getUniqueId() + ".chapter", 0);
    }

    public void setChapter(Player p, int chapter) {
        FileConfiguration config = plugin.getConfig();
        config.set("players." + p.getUniqueId() + ".chapter", chapter);
        plugin.saveConfig();
    }
}
