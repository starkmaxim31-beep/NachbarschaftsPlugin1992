package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NachbarschaftsPlugin Phase 4 aktiviert!");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin deaktiviert.");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können das benutzen!");
            return true;
        }

        Player p = (Player) sender;


        // -------------------------------
        // SANCTUM TELEPORT
        // -------------------------------
        if (cmd.getName().equalsIgnoreCase("sanctum")) {

            Location sanctum = getLocation("sanctum");
            if (sanctum == null) {
                p.sendMessage("§cSanctum nicht in config gefunden!");
                return true;
            }

            p.teleport(sanctum);
            p.sendTitle("§fSanctum der Admins", "§7Du betrittst die wahre Macht...", 10, 80, 10);
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 0.5f);

            return true;
        }


        // -------------------------------
        // ADMIN STORY
        // -------------------------------
        if (cmd.getName().equalsIgnoreCase("adminstory")) {

            p.sendMessage("§6[Gelber Admin] §fDie Welt verändert sich...");
            p.sendMessage("§a[Grüner Admin] §fWir müssen aufpassen...");
            p.sendMessage("§5[Lila Admin] §fEtwas Großes kommt...");
            p.sendMessage("§7Mysteriöser Spieler beobachtet dich...");

            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);

            return true;
        }


        // -------------------------------
        // ADMIN HILFE
        // -------------------------------
        if (cmd.getName().equalsIgnoreCase("adminhilfe")) {

            Bukkit.broadcastMessage("§c⚠ Ein Admin greift ein...");

            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.DARKNESS, 60, 1
                ));
            }

            p.sendMessage("§eEin Admin ist erschienen... aber du siehst ihn nicht richtig.");
            return true;
        }


        // -------------------------------
        // ADMIN VERWANDLUNG
        // -------------------------------
        if (cmd.getName().equalsIgnoreCase("adminform")) {

            p.sendTitle("§6Du veränderst dich...", "§eDie Admin Macht erwacht...", 10, 80, 10);
            p.getWorld().strikeLightningEffect(p.getLocation());
            p.getWorld().spawnParticle(Particle.ENCHANT, p.getLocation(), 200);
            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);

            Bukkit.getScheduler().runTaskLater(this, () ->
                    p.sendMessage("§f[Weißer Admin] Du bist auf dem richtigen Weg..."), 80);

            return true;
        }

        return false;
    }


    private Location getLocation(String path) {

        String worldName = getConfig().getString(path + ".world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) return null;

        double x = getConfig().getDouble(path + ".x");
        double y = getConfig().getDouble(path + ".y");
        double z = getConfig().getDouble(path + ".z");

        return new Location(world, x, y, z);
    }
}


