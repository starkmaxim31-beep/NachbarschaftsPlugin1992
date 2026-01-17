package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Nachbarschaft Plugin aktiv!");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Nachbarschaft Plugin deaktiviert!");
    }

    // ===================== COMMANDS =====================

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Nur Spieler!");
            return true;
        }

        World w = Bukkit.getWorld("world");

        switch (cmd.getName().toLowerCase()) {

            case "sanctum" -> {
                p.teleport(new Location(w, 200, 100, 200));
                p.sendMessage(ChatColor.DARK_PURPLE + "Du betrittst das Sanctum der Admins...");
            }

            case "sanctumwarn" -> {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "⚠ Das Sanctum reagiert auf eine Störung!");
            }

            case "stadtcheck" -> {
                p.sendMessage(ChatColor.GREEN + "Oberstadt: X=100 Y=80 Z=100");
                p.sendMessage(ChatColor.GRAY + "Unterstadt: X=-100 Y=40 Z=-100");
            }

            case "adminpalast" -> {
                p.teleport(new Location(w, 0, 120, 0));
                p.sendMessage(ChatColor.YELLOW + "Willkommen im Admin-Palast.");
            }

            case "adminform" -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 9999, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 9999, 2));
                p.sendMessage(ChatColor.GOLD + "Deine Adminkraft fließt durch dich.");
            }

            case "seelenstart" -> {
                if (p.hasMetadata("soulStarted")) {
                    p.sendMessage(ChatColor.RED + "Deine Seele ist bereits erwacht.");
                    return true;
                }
                p.setMetadata("soulStarted", new FixedMetadataValue(this, true));
                p.sendMessage(ChatColor.AQUA + "✨ Deine Seele beginnt zu erwachen...");
                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                p.spawnParticle(Particle.END_ROD, p.getLocation(), 60, 1, 1, 1);
            }

            case "waffe" -> {
                giveSoulSword(p);
                giveSoulBow(p);
                p.sendMessage(ChatColor.GREEN + "Deine Seelenwaffen wurden manifestiert.");
            }

            case "prüfung" -> {
                p.sendTitle(ChatColor.RED + "PRÜFUNG", ChatColor.GRAY + "Beweise deine Stärke", 10, 70, 20);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 400, 1));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            }

            case "kapitel" -> {
                int chapter = p.hasMetadata("chapter") ? p.getMetadata("chapter").get(0).asInt() : 1;

                String[] chapters = {
                        "Kapitel 1 – Ankunft in der Oberstadt",
                        "Kapitel 2 – Die Unterstadt",
                        "Kapitel 3 – Der verrückte Professor",
                        "Kapitel 4 – Die Seelenwaffe erwacht",
                        "Kapitel 5 – Die ersten Admins",
                        "Kapitel 6 – Der Admin-Palast",
                        "Kapitel 7 – Das Sanctum",
                        "Kapitel 8 – Der gelbe Admin kippt",
                        "Kapitel 9 – Die mysteriöse Figur",
                        "Kapitel 10 – Ritual der Materie",
                        "Kapitel 11 – Ritual der Dunkelheit",
                        "Kapitel 12 – Wahrheit über Lila & Grün",
                        "Kapitel 13 – Der gelbe Admin fällt",
                        "Kapitel 14 – Kriegsbeginn",
                        "Kapitel 15 – Wahrheit der Admins",
                        "Kapitel 16 – Ultimativer Commandblock",
                        "Kapitel 17 – Sanctum bricht",
                        "Kapitel 18 – Deine Verwandlung",
                        "Kapitel 19 – Letzter Konflikt",
                        "Kapitel 20 – Entscheidung"
                };

                if (chapter <= chapters.length) {
                    p.sendMessage(ChatColor.GOLD + "📖 " + chapters[chapter - 1]);
                    p.setMetadata("chapter", new FixedMetadataValue(this, chapter + 1));
                } else {
                    p.sendMessage(ChatColor.DARK_PURPLE + "Du hast alle Kapitel abgeschlossen.");
                }
            }

            case "adminstory" -> {
                p.sendTitle(ChatColor.DARK_PURPLE + "Die Admins...",
                        ChatColor.GRAY + "beobachten dich",
                        10, 80, 10);
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
            }

            case "adminhelp" -> {
                p.sendMessage(ChatColor.YELLOW + "==== ADMIN HILFE ====");
                p.sendMessage("/seelenstart");
                p.sendMessage("/waffe");
                p.sendMessage("/kapitel");
                p.sendMessage("/prüfung");
                p.sendMessage("/sanctum");
            }
        }

        return true;
    }

    // ===================== SEELENWAFFEN =====================

    private void giveSoulSword(Player p) {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "◆ Seelenklinge ◆");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Gebunden an: " + p.getName(),
                ChatColor.DARK_PURPLE + "Seelengebunden – unzerstörbar"
        ));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        p.getInventory().addItem(sword);
    }

    private void giveSoulBow(Player p) {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "✦ Seelenbogen ✦");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Erzeugt Pfeilregen",
                ChatColor.DARK_PURPLE + "Superskill: Seelensturz"
        ));
        meta.setUnbreakable(true);
        bow.setItemMeta(meta);

        p.getInventory().addItem(bow);
    }

    // ===================== BOGEN-FÄHIGKEIT =====================

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (!e.getBow().hasItemMeta()) return;

        ItemMeta meta = e.getBow().getItemMeta();
        if (!meta.getDisplayName().contains("Seelenbogen")) return;

        Location loc = p.getLocation();
        World w = p.getWorld();

        // Pfeilregen
        for (int i = 0; i < 10; i++) {
            Arrow a = w.spawnArrow(
                    loc.clone().add(Math.random() * 2 - 1, 5, Math.random() * 2 - 1),
                    new Vector(0, -1, 0),
                    1.5f,
                    0
            );
            a.setCritical(true);
        }

        // Rückstoß + Kurzflug
        p.setVelocity(p.getLocation().getDirection().multiply(-0.6).setY(0.6));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 1));

        p.playSound(loc, Sound.ENTITY_WITHER_SHOOT, 1, 1);
    }
}
