package de.nachbarschaft;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NachbarschaftsPlugin1992 gestartet!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NachbarschaftsPlugin1992 gestoppt!");
    }
}
