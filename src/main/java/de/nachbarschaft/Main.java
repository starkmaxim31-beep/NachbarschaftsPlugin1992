package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NachbarschaftsPlugin – Phase 6 geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NachbarschaftsPlugin deaktiviert.");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können das benutzen.");
            return true;
        }

        Player p = (Player) sender;

        // ----------- KAPITEL SYSTEM ----------
        if (cmd.getName().equalsIgnoreCase("kapitel")) {
            p.sendMessage(ChatColor.GOLD + "------------------------------------------------");
            p.sendMessage(ChatColor.YELLOW + "Nachbarschaft – Kapitel System aktiviert!");
            p.sendMessage(ChatColor.AQUA + "Du befindest dich aktuell in: " + ChatColor.GREEN + "Kapitel 1");
            p.sendMessage(ChatColor.GRAY + "Fortschritt folgt automatisch in späteren Phasen.");
            p.sendMessage(ChatColor.GOLD + "------------------------------------------------");
            return true;
        }

        // ----------- SANCTUM ----------
        if (cmd.getName().equalsIgnoreCase("sanctum")) {

            World world = Bukkit.getWorld("world");

            if (world == null) {
                p.sendMessage(ChatColor.RED + "Konnte Welt nicht finden!");
                return true;
            }

            Location sanctum = new Location(world, 0, 150, 0);
            p.teleport(sanctum);

            p.sendMessage(ChatColor.DARK_PURPLE + "------------------------------------");
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Du betrittst das Sanctum der Admins...");
            p.sendMessage(ChatColor.GRAY + "Ein Ort zwischen Realität und Macht.");
            p.sendMessage(ChatColor.DARK_PURPLE + "------------------------------------");

            return true;
        }

        // ----------- TEST ----------
        if (cmd.getName().equalsIgnoreCase("nachbarschafttest")) {
            p.sendMessage(ChatColor.GREEN + "Plugin funktioniert! Commands sind aktiv.");
            return true;
        }

        return false;
    }
}
