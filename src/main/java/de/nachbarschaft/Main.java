package de.nachbarschaft;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private KapitelManager kapitelManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        kapitelManager = new KapitelManager(this);

        getCommand("kapitel").setExecutor(new KapitelCommand(kapitelManager));

        getLogger().info("Phase 2 Story System aktiviert!");
    }

    public static Main getInstance() {
        return instance;
    }

    public KapitelManager getKapitelManager() {
        return kapitelManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("Phase 2 System deaktiviert.");
    }
}

