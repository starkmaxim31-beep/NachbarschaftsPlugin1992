package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("PHASE 6 Aktiv!");
    }

    // ------------------- STORY SYSTEM -------------------
    private void nextChapter(Player p) {

        if (!p.hasMetadata("chapter"))
            p.setMetadata("chapter", new FixedMetadataValue(this, 1));

        int chapter = p.getMetadata("chapter").get(0).asInt();

        switch (chapter) {

            case 1 -> sendChapter(p, "Kapitel 1 – Ankunft in der Oberstadt",
                    "Du spürst, dass hier etwas Größeres beginnt…");

            case 2 -> sendChapter(p, "Kapitel 2 – Die Unterstadt",
                    "Dunkle Geheimnisse beginnen sich zu bewegen…");

            case 3 -> sendChapter(p, "Kapitel 3 – Erste Admin-Spuren",
                    "Etwas überwacht diese Welt…");

            case 4 -> sendChapter(p, "Kapitel 4 – Der Gelbe Admin",
                    "Er wird misstrauisch…");

            case 5 -> sendChapter(p, "Kapitel 5 – Sanctum Erwacht",
                    "Das Sanctum der Admins beginnt zu reagieren!");

            case 6 -> sendChapter(p, "Kapitel 6 – Deine Seele erwacht",
                    "/seelenstart könnte jetzt wichtig sein…");

            case 7 -> sendChapter(p, "Kapitel 7 – Prüfung des Willens",
                    "/prüfung wird dich testen");

            case 8 -> sendChapter(p, "Kapitel 8 – Schatten in der Unterstadt",
                    "Der mysteriöse Spieler beobachtet dich…");

            case 9 -> sendChapter(p, "Kapitel 9 – Adminwarnung",
                    "/sanctumwarn sendet eine Warnung an die Welt");

            case 10 -> sendChapter(p, "Kapitel 10 – Admin Palast",
                    "Der Palast ruft dich… /adminpalast");

            case 11 -> sendChapter(p, "Kapitel 11 – Wahre Macht",
                    "Du spürst die Adminenergie in dir…");

            case 12 -> sendChapter(p, "Kapitel 12 – Transformation",
                    "/adminform erweckt deine Kraft");

            case 13 -> sendChapter(p, "Kapitel 13 – Seelenbindung",
                    "/waffe bringt dir deine Seelenklinge");

            case 14 -> sendChapter(p, "Kapitel 14 – Oberstadt vs Unterstadt",
                    "/stadtcheck überprüft alles");

            case 15 -> sendChapter(p, "Kapitel 15 – Gefahr steigt",
                    "Etwas ganz Großes kommt…");

            case 16 -> sendChapter(p, "Kapitel 16 – Verrat",
                    "Der Gelbe Admin wird endgültig böse…");

            case 17 -> sendChapter(p, "Kapitel 17 – Sanctum Entscheidung",
                    "Die Admins beraten über dich…");

            case 18 -> sendChapter(p, "Kapitel 18 – Schicksal",
                    "Die Welt hängt von dir ab…");

            case 19 -> sendChapter(p, "Kapitel 19 – Finale Vorbereitung",
                    "Alles führt zum Ende…");

            case 20 -> sendChapter(p, "Kapitel 20 – Finale",
                    "Das Schicksal der Welt entscheidet sich jetzt!");

            default -> {
                p.sendMessage(ChatColor.GREEN + "🎉 DU HAST ALLE 20 KAPITEL ABGESCHLOSSEN!");
                return;
            }
        }

        p.setMetadata("chapter", new FixedMetadataValue(this, chapter + 1));
    }

    private void sendChapter(Player p, String title, String desc) {
        p.sendTitle(ChatColor.GOLD + title, ChatColor.GRAY + desc, 10, 80, 20);
        p.sendMessage(ChatColor.YELLOW + "-------------------------");
        p.sendMessage(ChatColor.GOLD + "📖 " + title);
        p.sendMessage(ChatColor.GRAY + desc);
        p.sendMessage(ChatColor.YELLOW + "-------------------------");
    }

    // ---------------- COMMANDS ----------------
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler!");
            return true;
        }

        Player p = (Player) sender;
        World w = Bukkit.getWorld("world");

        switch (cmd.getName().toLowerCase()) {

            case "kapitel" -> nextChapter(p);

            case "sanctum" -> {
                p.teleport(new Location(w, 200, 100, 200));
                p.sendMessage(ChatColor.DARK_PURPLE + "Du betrittst das Sanctum der Admins...");
            }

            case "sanctumwarn" ->
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "⚠ Ein Admin spürt Unruhe... Das Sanctum reagiert!");

            case "stadtcheck" -> {
                p.sendMessage(ChatColor.GREEN + "Oberstadt: X=100 Y=80 Z=100");
                p.sendMessage(ChatColor.GRAY + "Unterstadt: X=-100 Y=40 Z=-100");
            }

            case "adminpalast" -> {
                p.teleport(new Location(w, 0, 120, 0));
                p.sendMessage(ChatColor.YELLOW + "Willkommen im Admin Palast!");
            }

            case "adminform" -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 999999, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));
                p.sendMessage(ChatColor.YELLOW + "🔥 Deine Adminkraft erwacht!");
            }

            case "seelenstart" -> {
                if (p.hasMetadata("soulStarted")) {
                    p.sendMessage(ChatColor.RED + "Deine Seele wurde bereits erweckt.");
                    return true;
                }

                p.setMetadata("soulStarted", new FixedMetadataValue(this, true));
                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                p.spawnParticle(Particle.END_ROD, p.getLocation(), 50, 1, 1, 1);
                p.sendMessage(ChatColor.AQUA + "✨ Deine Seele beginnt sich zu öffnen…");
            }

            case "waffe" -> {
                if (p.hasMetadata("soulWeaponGiven")) {
                    p.sendMessage(ChatColor.RED + "Du besitzt deine Seelenwaffe bereits!");
                    return true;
                }

                giveSoulWeapon(p);
                p.setMetadata("soulWeaponGiven", new FixedMetadataValue(this, true));
            }

            case "prüfung" -> {
                p.sendMessage(ChatColor.BLUE + "⚔ Die Prüfung wurde gestartet!");
                p.sendTitle(ChatColor.RED + "PRÜFUNG", ChatColor.GRAY + "Beweise deine Stärke!", 10, 70, 20);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);

                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 400, 1));
           
            case "adminstory":
                p.sendTitle(ChatColor.DARK_PURPLE + "Die Admins...", ChatColor.GRAY + "etwas stimmt nicht...", 10, 80, 10);
                p.sendMessage(ChatColor.DARK_PURPLE + "⚡ Du spürst eine Macht in der Welt...");
                p.sendMessage(ChatColor.GRAY + "Gerüchte erzählen von einem Ort namens " + ChatColor.YELLOW + "Sanctum der Admins");
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
   
            }
        }

        return true;
    }

    private void giveSoulWeapon(Player p) {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "◆ Seelenklinge ◆");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Gebunden an: " + p.getName(),
                ChatColor.DARK_PURPLE + "Unzerstörbar – Seelenmacht"
        ));
        meta.setUnbreakable(true);

        sword.setItemMeta(meta);
        p.getInventory().addItem(sword);

        p.sendMessage(ChatColor.GREEN + "✔ Deine Seelenwaffe wurde dir gegeben!");
    }
}
