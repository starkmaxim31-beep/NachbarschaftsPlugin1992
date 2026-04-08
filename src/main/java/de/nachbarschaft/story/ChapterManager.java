package de.nachbarschaft.story;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import de.nachbarschaft.story.events.StoryTriggerManager;
private StoryTriggerManager triggerManager = new StoryTriggerManager();

public class ChapterManager {

    private JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    private HashMap<UUID, Integer> chapterMap = new HashMap<>();

    public ChapterManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadFile();
    }

    private void loadFile() {
        file = new File(plugin.getDataFolder(), "chapters.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public int getChapter(UUID uuid) {
        return chapterMap.getOrDefault(uuid, 1);
    }

 public void setChapter(UUID uuid, int chapter) {

    chapterMap.put(uuid, chapter);

    config.set(uuid.toString(), chapter);

    save();

    Player player = Bukkit.getPlayer(uuid);

    if (player != null) {
        triggerManager.checkChapterTrigger(player, chapter);
    }

}

    public void loadChapters() {
        for (String key : config.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            int chapter = config.getInt(key);
            chapterMap.put(uuid, chapter);
        }
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
