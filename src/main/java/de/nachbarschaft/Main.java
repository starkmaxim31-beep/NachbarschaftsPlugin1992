package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.metadata.FixedMetadataValue;

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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler!");
            return true;
        }

        Player p = (Player) sender;
        World w = Bukkit.getWorld("world");

        switch (cmd.getName().toLowerCase()) {

            case "sanctum" -> {
                p.teleport(new Location(w, 200, 100, 200));
                p.sendMessage(ChatColor.DARK_PURPLE + "Du betrittst das Sanctum der Admins...");
            }

            case "sanctumwarn" -> {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "⚠ Ein Admin spürt Unruhe... Das Sanctum reagiert!");
            }

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
                p.sendMessage(ChatColor.YELLOW + "Deine Adminkraft erwacht!");
            }

            case "seelenstart" -> {
                if (p.hasMetadata("soulStarted")) {
                    p.sendMessage(ChatColor.RED + "Deine Seele wurde bereits erweckt.");
                    return true;
                }

                p.setMetadata("soulStarted", new FixedMetadataValue(this, true));
                p.sendMessage(ChatColor.AQUA + "✨ Deine Seele beginnt sich zu öffnen…");
                p.sendMessage(ChatColor.GRAY + "Du spürst Macht... aber sie ist noch nicht vollständig...");

                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
                p.spawnParticle(Particle.END_ROD, p.getLocation(), 50, 1, 1, 1);
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
            }

            case "kapitel" -> {
                if (!p.hasMetadata("chapter")) {
                    p.setMetadata("chapter", new FixedMetadataValue(this, 1));
                }

                int chapter = p.getMetadata("chapter").get(0).asInt();

                switch (chapter) {
                    case 1 -> {
                        p.sendMessage(ChatColor.GOLD + "📖 Kapitel 1 – Ankunft in der Oberstadt");
                        p.sendMessage(ChatColor.GRAY + "Deine Geschichte beginnt…");
                    }
                    case 2 -> p.sendMessage(ChatColor.GOLD + "📖 Kapitel 2 – Die Unterstadt und ihre Geheimnisse");
                    case 3 -> p.sendMessage(ChatColor.GOLD + "📖 Kapitel 3 – Die ersten Admin-Hinweise erscheinen…");
                    default -> {
                        p.sendMessage(ChatColor.GREEN + "🎉 Du hast alle Kapitel abgeschlossen!");
                        return true;
                    }
                }

                p.setMetadata("chapter", new FixedMetadataValue(this, chapter + 1));
            }

            case "adminstory" -> {
                p.sendTitle(ChatColor.DARK_PURPLE + "Die Admins...",
                        ChatColor.GRAY + "etwas stimmt nicht...",
                        10, 80, 10);

                p.sendMessage(ChatColor.DARK_PURPLE + "⚡ Du spürst eine Macht in der Welt...");
                p.sendMessage(ChatColor.GRAY + "Gerüchte erzählen von einem Ort namens "
                        + ChatColor.YELLOW + "Sanctum der Admins");

                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
            }

            case "adminhelp" -> {
                p.sendMessage(ChatColor.YELLOW + "==== ADMIN HILFE ====");
                p.sendMessage("/adminstory - startet Admin Story");
                p.sendMessage("/sanctum - teleport Sanctum");
                p.sendMessage("/prüfung - Prüfung starten");
                p.sendMessage("/waffe - Seelenwaffe");
                p.sendMessage("/kapitel - Story Kapitel");
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
