package de.nachbarschaft;

import org.bukkit.plugin.java.JavaPlugin;

import de.nachbarschaft.story.ChapterManager;
import de.nachbarschaft.commands.ChapterCommand;
import de.nachbarschaft.trigger.PlayerMoveListener;
import de.nachbarschaft.soulweapons.SoulWeaponManager;
import de.nachbarschaft.soulweapons.SoulWeaponListener;

public class Main extends JavaPlugin {

    // Singleton Instance
    private static Main instance;

    // Manager
    private ChapterManager chapterManager;
    private SoulWeaponManager soulWeaponManager;

    @Override
    public void onEnable() {

        // Instance setzen
        instance = this;

        // Chapter Manager starten
        chapterManager =
                new ChapterManager(this);

        // SoulWeapon Manager starten
        soulWeaponManager =
                new SoulWeaponManager();

        // Command registrieren
        if (getCommand("chapter") != null) {

            getCommand("chapter")
                    .setExecutor(
                            new ChapterCommand(
                                    chapterManager
                            )
                    );

        }

        // Movement Listener registrieren
        getServer()
                .getPluginManager()
                .registerEvents(
                        new PlayerMoveListener(
                                chapterManager
                        ),
                        this
                );

        getLogger()
                .info(
                        "NachbarschaftsPlugin gestartet!"
                );

    }

    @Override
    public void onDisable() {

        getLogger()
                .info(
                        "NachbarschaftsPlugin gestoppt!"
                );

    }

    // Instance Getter
    public static Main getInstance() {
        return instance;
    }

    // Chapter Manager Getter
    public ChapterManager getChapterManager() {
        return chapterManager;
    }

    // SoulWeapon Manager Getter
    public SoulWeaponManager getSoulWeaponManager() {
        return soulWeaponManager;
    }

}
