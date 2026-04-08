package de.nachbarschaft;

import org.bukkit.plugin.java.JavaPlugin;
import de.nachbarschaft.story.ChapterManager;
import de.nachbarschaft.commands.ChapterCommand;

public class Main extends JavaPlugin {

    private static Main instance;
    private ChapterManager chapterManager;

  @Override
public void onEnable() {

    instance = this;

    getLogger().info("NachbarschaftsPlugin gestartet!");

    chapterManager = new ChapterManager(this);

    getCommand("chapter").setExecutor(new ChapterCommand());

}

    @Override
    public void onDisable() {

        getLogger().info("NachbarschaftsPlugin gestoppt!");

    }

    public static Main getInstance() {
        return instance;
    }

    public ChapterManager getChapterManager() {
        return chapterManager;
    }
}
