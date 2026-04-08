package de.nachbarschaft.story;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.nachbarschaft.story.events.StoryTriggerManager;

public class ChapterManager {

    private JavaPlugin plugin;

    private Map<UUID, Integer> chapterMap = new HashMap<>();

    private File file;
    private YamlConfiguration config;

    private StoryTriggerManager triggerManager =
            new StoryTriggerManager();

    public ChapterManager(JavaPlugin plugin) {

        this.plugin = plugin;

        file = new File(
                plugin.getDataFolder(),
                "chapters.yml"
        );

        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(
                    "chapters.yml",
                    false
            );
        }

        config = YamlConfiguration.loadConfiguration(file);

        load();

    }

    public int getChapter(UUID uuid) {

        return chapterMap.getOrDefault(uuid, 1);

    }

    public void setChapter(
            UUID uuid,
            int chapter
    ) {

        chapterMap.put(uuid, chapter);

        config.set(
                uuid.toString(),
                chapter
        );

        save();

        Player player =
                Bukkit.getPlayer(uuid);

        if (player != null) {

            triggerManager
                    .checkChapterTrigger(
                            player,
                            chapter
                    );

        }

    }

    private void load() {

        for (String key :
                config.getKeys(false)) {

            UUID uuid =
                    UUID.fromString(key);

            int chapter =
                    config.getInt(key);

            chapterMap.put(
                    uuid,
                    chapter
            );

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
