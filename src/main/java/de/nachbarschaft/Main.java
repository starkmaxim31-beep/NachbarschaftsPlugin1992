package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    int kapitel = 1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        kapitel = getConfig().getInt("kapitel.current");
        getLogger().info("NachbarschaftsPlugin Phase 6 aktiviert!");
    }

    @Override
    public void onDisable() {
        getConfig().set("kapitel.current", kapitel);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        // ----------------- Kapitel System -----------------
        if (cmd.getName().equalsIgnoreCase("kapitel")) {

            if (args.length == 0) {
                p.sendMessage("§eAktuelles Kapitel: §b" + kapitel);
                return true;
            }

            if (args[0].equalsIgnoreCase("start")) {
                kapitel = 1;
                p.sendMessage("§aKapitel 1 gestartet! Oberstadt & Unterstadt sind jetzt aktiv!");
                return true;
            }

            if (args[0].equalsIgnoreCase("weiter")) {
                kapitel++;
                p.sendMessage("§aKapitel fortgesetzt → Kapitel " + kapitel);
                return true;
            }
        }

        // ----------------- Admin Palast -----------------
        if (cmd.getName().equalsIgnoreCase("adminpalast")) {
            World w = Bukkit.getWorlds().get(0);
            p.teleport(new Location(w, 100, 120, 100));
            p.sendMessage("§6Du betrittst den Admin Palast...");
            return true;
        }

        // ----------------- Sanctum -----------------
        if (cmd.getName().equalsIgnoreCase("sanctum")) {
            World w = Bukkit.getWorlds().get(0);
            p.teleport(new Location(w, 500, 150, 500));
            p.sendMessage("§5Du fühlst die Macht der Admins...");
            return true;
        }

        // ----------------- Ritual NUR im Nether -----------------
        if (cmd.getName().equalsIgnoreCase("ritual")) {

            if (!p.getWorld().getName().toLowerCase().contains("nether")) {
                p.sendMessage("§cDas Ritual kann NUR im Nether stattfinden!");
                return true;
            }

            p.sendMessage("§4Das gefährliche Ritual beginnt...");
            p.getWorld().strikeLightningEffect(p.getLocation());
            p.getWorld().createExplosion(p.getLocation(), 0, false, false);
            return true;
        }

        return true;
    }
}
