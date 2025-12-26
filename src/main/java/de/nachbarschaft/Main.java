package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    private final HashMap<UUID, Integer> kapitelFortschritt = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("NachbarschaftsPlugin Phase 6 aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NachbarschaftsPlugin deaktiviert!");
    }

    // ----------------------------------------------------------
    // KAPITEL HUD SYSTEM
    // ----------------------------------------------------------
    private void startKapitelHUD(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("kapitel", Criteria.DUMMY, ChatColor.GOLD + "Kapitel");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int fortschritt = kapitelFortschritt.getOrDefault(p.getUniqueId(), 1);

        obj.getScore(ChatColor.YELLOW + "Aktuelles Kapitel:").setScore(3);
        obj.getScore(ChatColor.GREEN + "Kapitel " + fortschritt).setScore(2);
        obj.getScore(ChatColor.GRAY + "Story aktiv!").setScore(1);

        p.setScoreboard(board);
    }

    // ----------------------------------------------------------
    // COMMANDS
    // ----------------------------------------------------------
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können das benutzen.");
            return true;
        }

        Player p = (Player) sender;

        // ------------------ /kapitel ------------------
        if (cmd.getName().equalsIgnoreCase("kapitel")) {

            kapitelFortschritt.putIfAbsent(p.getUniqueId(), 1);
            startKapitelHUD(p);

            p.sendMessage(ChatColor.GOLD + "--------------------------------");
            p.sendMessage(ChatColor.AQUA + "Kapitel System aktiviert!");
            p.sendMessage(ChatColor.GREEN + "Du bist aktuell in Kapitel: " +
                    kapitelFortschritt.get(p.getUniqueId()));
            p.sendMessage(ChatColor.GRAY + "Fortschritt folgt durch Story!");
            p.sendMessage(ChatColor.GOLD + "--------------------------------");
            return true;
        }

        // ------------------ /sanctum ------------------
        if (cmd.getName().equalsIgnoreCase("sanctum")) {
            World world = Bukkit.getWorld("world");

            if (world == null) {
                p.sendMessage(ChatColor.RED + "Welt konnte nicht gefunden werden!");
                return true;
            }

            Location sanctum = new Location(world, 0, 150, 0);
            p.teleport(sanctum);

            p.sendMessage(ChatColor.DARK_PURPLE + "------------------------------------");
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Du betrittst das Sanctum der Admins...");
            p.sendMessage(ChatColor.GRAY + "Zwischen Realität und Macht.");
            p.sendMessage(ChatColor.DARK_PURPLE + "------------------------------------");

            return true;
        }

        // ------------------ /prüfung ------------------
        if (cmd.getName().equalsIgnoreCase("prüfung")) {

            p.sendMessage(ChatColor.RED + "Die Adminprüfung beginnt...");
            p.sendMessage(ChatColor.GRAY + "Du wirst getestet... bestehe die Prüfung!");

            new BukkitRunnable() {
                int timer = 5;

                @Override
                public void run() {
                    if (timer == 0) {
                        p.sendMessage(ChatColor.GREEN + "✔ Prüfung bestanden!");
                        kapitelFortschritt.put(p.getUniqueId(), 6);
                        startKapitelHUD(p);
                        cancel();
                        return;
                    }

                    p.sendTitle(
                            ChatColor.GOLD + "Prüfung",
                            ChatColor.YELLOW + "Noch " + timer + " Sekunden...",
                            10, 20, 10
                    );

                    timer--;
                }

            }.runTaskTimer(this, 0, 20);

            return true;
        }

        // ------------------ /nachbarschafttest ------------------
        if (cmd.getName().equalsIgnoreCase("nachbarschafttest")) {
            p.sendMessage(ChatColor.GREEN + "Plugin läuft. Commands aktiv.");
            return true;
        }

        return false;
    }
}


