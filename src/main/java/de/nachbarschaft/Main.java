package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NachbarschaftsPlugin aktiviert!");

        // Falls später Config benötigt wird
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("NachbarschaftsPlugin deaktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl ist nur für Spieler.");
            return true;
        }

        Player p = (Player) sender;

        switch (cmd.getName().toLowerCase()) {

            case "kapitel":
                p.sendMessage(ChatColor.GOLD + "Kapitel System aktiv!");
                return true;

            case "sanctum":
                p.sendMessage(ChatColor.DARK_PURPLE + "Sanctum System aktiviert!");
                return true;

            case "ritual":
                p.sendMessage(ChatColor.RED + "Ritual gestartet!");
                return true;

            case "waffe":
                ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
                ItemMeta meta = sword.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "Schlüsselschwert");
                sword.setItemMeta(meta);
                p.getInventory().addItem(sword);
                p.sendMessage(ChatColor.GREEN + "Du hast deine Seelenwaffe erhalten!");
                return true;

            case "adminform":
                p.sendMessage(ChatColor.YELLOW + "Admin Transformation gestartet!");
                p.setHealth(20);
                p.setAllowFlight(true);
                return true;

            case "prüfung":
                p.sendMessage(ChatColor.BLUE + "Prüfung gestartet!");
                return true;

            case "adminstory":
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Die Geschichte der Admins beginnt…");
                return true;

            case "stadtcheck":
                p.sendMessage(ChatColor.GREEN + "Stadtstatus überprüft!");
                return true;

            case "adminpalast":
                World w = Bukkit.getWorld("world");
                Location loc = new Location(w, 0, 100, 0);
                p.teleport(loc);
                p.sendMessage(ChatColor.GOLD + "Willkommen im Admin Palast!");
                return true;
        }

        return false;
    }
}


