package de.nachbarschaft;

import org.bukkit.plugin.java.JavaPlugin;

import de.nachbarschaft.story.ChapterManager;
import de.nachbarschaft.commands.ChapterCommand;
import de.nachbarschaft.trigger.PlayerMoveListener;

import de.nachbarschaft.soulweapons.SoulWeaponManager;
import de.nachbarschaft.soulweapons.SoulWeaponListener;

public class Main extends JavaPlugin {

    private static Main instance;

    private ChapterManager chapterManager;
    private SoulWeaponManager soulWeaponManager;

    @Override
    public void onEnable() {

        instance = this;

        // Manager starten
        chapterManager =
                new ChapterManager();

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

        // Movement Listener
        getServer()
                .getPluginManager()
                .registerEvents(
                        new PlayerMoveListener(
                                chapterManager
                        ),
                        this
                );

        // SoulWeapon Listener
        getServer()
                .getPluginManager()
                .registerEvents(
                        new SoulWeaponListener(),
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

    public static Main getInstance() {
        return instance;
    }

    public ChapterManager getChapterManager() {
        return chapterManager;
    }

    public SoulWeaponManager getSoulWeaponManager() {
        return soulWeaponManager;
    }

}
