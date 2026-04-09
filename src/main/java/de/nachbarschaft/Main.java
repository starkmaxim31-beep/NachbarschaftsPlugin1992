package de.nachbarschaft;

import de.nachbarschaft.commands.ChapterCommand;
import de.nachbarschaft.story.ChapterManager;
import de.nachbarschaft.trigger.ZoneTrigger;
import de.nachbarschaft.soulweapons.SoulWeaponManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private ChapterManager chapterManager;
    private SoulWeaponManager soulWeaponManager;

    @Override
    public void onEnable() {

        instance = this;

        // Manager starten
        chapterManager = new ChapterManager();
        soulWeaponManager = new SoulWeaponManager();

        // Command registrieren
        getCommand("chapter").setExecutor(
                new ChapterCommand()
        );

        // Listener registrieren
        getServer().getPluginManager().registerEvents(
                new ZoneTrigger(),
                this
        );

        getLogger().info("NachbarschaftsPlugin gestartet!");
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
