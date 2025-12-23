package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NachbarschaftsPlugin1992 Phase 3 aktiviert!");

        // erzeugt config.yml automatisch
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin deaktiviert.");
    }


    // --------------------------
    //        COMMANDS
    // --------------------------

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Sicherstellen dass ein Spieler den Befehl nutzt
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        Player p = (Player) sender;


        // ------------------------------------------
        // /kapitelstart → teleportiert in Oberstadt
        // ------------------------------------------
        if (cmd.getName().equalsIgnoreCase("kapitelstart")) {

            Location oberstadt = getLocationFromConfig("locations.oberstadt");

            if (oberstadt == null) {
                p.sendMessage("§cFehler: Oberstadt-Koordinaten fehlen in der config.yml!");
                return true;
            }

            p.teleport(oberstadt);
            p.sendMessage("§aDas Kapitel beginnt! Willkommen in der Oberstadt.");

            return true;
        }


        // ------------------------------------------
        // /stadtcheck → Zeigt die gespeicherten Orte
        // ------------------------------------------
        if (cmd.getName().equalsIgnoreCase("stadtcheck")) {

            Location o = getLocationFromConfig("locations.oberstadt");
            Location u = getLocationFromConfig("locations.unterstadt");

            p.sendMessage("§e--- Städte Übersicht ---");
            if (o != null)
                p.sendMessage("§aOberstadt: §f" + o.getWorld().getName() + " " + o.getX() + " " + o.getY() + " " + o.getZ());
            if (u != null)
                p.sendMessage("§bUnterstadt: §f" + u.getWorld().getName() + " " + u.getX() + " " + u.getY() + " " + u.getZ());

            return true;
        }

        return false;
    }


    // ------------------------------------------
    //     Holt eine Location aus config.yml
    // ------------------------------------------
    private Location getLocationFromConfig(String path) {

        String worldName = getConfig().getString(path + ".world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) return null;

        double x = getConfig().getDouble(path + ".x");
        double y = getConfig().getDouble(path + ".y");
        double z = getConfig().getDouble(path + ".z");

        return new Location(world, x, y, z);
    }
}


