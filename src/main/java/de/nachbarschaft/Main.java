package de.nachbarschaft;

import org.bukkit.plugin.java.JavaPlugin;

import de.nachbarschaft.story.ChapterManager;
import de.nachbarschaft.commands.ChapterCommand;
import de.nachbarschaft.trigger.PlayerMoveListener;

public class Main extends JavaPlugin {

    private ChapterManager chapterManager;

    @Override
    public void onEnable() {

        // Chapter Manager starten
        chapterManager = new ChapterManager(this);

        // Command registrieren
        if (getCommand("chapter") != null) {

            getCommand("chapter").setExecutor(
                    new ChapterCommand(chapterManager)
            );

        }

        // Movement Listener registrieren
        getServer().getPluginManager().registerEvents(
                new PlayerMoveListener(chapterManager),
                this
        );

        getLogger().info(
                "NachbarschaftsPlugin gestartet!"
        );

    }

    @Override
    public void onDisable() {

        getLogger().info(
                "NachbarschaftsPlugin gestoppt!"
        );

    }

}
