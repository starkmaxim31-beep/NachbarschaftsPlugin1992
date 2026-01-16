package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Long> dashCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Nachbarschaft Plugin aktiv (ohne Citizens)");
    }

    /* ---------------- COMMANDS ---------------- */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Nur Spieler.");
            return true;
        }

        World w = Bukkit.getWorld("world");

        switch (cmd.getName().toLowerCase()) {

            case "waffe" -> giveSoulWeapon(p);

            case "kapitel" -> playNextChapter(p, w);

            case "sanctum" -> {
                p.teleport(new Location(w, 200, 100, 200));
                p.sendMessage(ChatColor.DARK_PURPLE + "Du betrittst das Sanctum der Admins…");
                p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1, 1);
            }

            case "adminpalast" -> {
                p.teleport(new Location(w, 0, 120, 0));
                p.sendMessage(ChatColor.GOLD + "Der Admin-Palast erhebt sich über dir.");
                p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
            }

            case "adminhelp" -> {
                p.sendMessage(ChatColor.YELLOW + "=== ADMIN HILFE ===");
                p.sendMessage("/waffe – Seelenwaffe erhalten");
                p.sendMessage("/kapitel – Story fortsetzen");
                p.sendMessage("/sanctum – Sanctum betreten");
                p.sendMessage("/adminpalast – Adminpalast");
            }
        }
        return true;
    }

    /* ---------------- SEELENWAFFE ---------------- */

    private void giveSoulWeapon(Player p) {

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "◆ Seelenklinge ◆");
        meta.setLore(List.of(
                ChatColor.GRAY + "Gebunden an: " + p.getName(),
                ChatColor.DARK_PURPLE + "Dash-Angriff · Seelenmacht"
        ));
        meta.setUnbreakable(true);

        sword.setItemMeta(meta);
        p.getInventory().addItem(sword);

        p.sendMessage(ChatColor.GREEN + "Deine Seelenklinge erwacht.");
        p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 1);
    }

    /* ---------------- DASH ---------------- */

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item.getType() != Material.NETHERITE_SWORD) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().getDisplayName().contains("Seelenklinge")) return;

        // ❌ kein Luft-Dash
        if (!p.isOnGround()) {
            p.sendMessage(ChatColor.RED + "Der Dash funktioniert nur am Boden.");
            return;
        }

        long now = System.currentTimeMillis();
        if (dashCooldown.containsKey(p.getUniqueId())
                && now - dashCooldown.get(p.getUniqueId()) < 3000) {
            p.sendMessage(ChatColor.GRAY + "Die Seelenklinge lädt sich neu…");
            return;
        }

        dashCooldown.put(p.getUniqueId(), now);

        Vector dir = p.getLocation().getDirection().normalize();
        p.setVelocity(dir.multiply(2.2));

        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        p.spawnParticle(Particle.CRIT, p.getLocation(), 30, 0.5, 0.5, 0.5);

        // Treffer während Dash
        Bukkit.getScheduler().runTaskTimer(this, task -> {

            for (Entity ent : p.getNearbyEntities(1.5, 1.5, 1.5)) {
                if (ent instanceof LivingEntity le && ent != p) {
                    le.damage(6.0, p);
                    le.setVelocity(le.getLocation().toVector()
                            .subtract(p.getLocation().toVector()).normalize().multiply(0.4));
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                }
            }

        }, 0L, 1L);

        Bukkit.getScheduler().runTaskLater(this, () ->
                Bukkit.getScheduler().cancelTasks(this), 10L);
    }

    /* ---------------- KAPITEL ---------------- */

    private void playNextChapter(Player p, World w) {

        int chapter = p.hasMetadata("chapter")
                ? p.getMetadata("chapter").get(0).asInt()
                : 1;

        switch (chapter) {

            case 1 -> {
                p.sendTitle("Kapitel 1", "Ankunft in der Oberstadt", 10, 60, 10);
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
            }
            case 2 -> {
                p.teleport(new Location(w, -100, 40, -100));
                p.sendTitle("Kapitel 2", "Die Unterstadt", 10, 60, 10);
            }
            case 3 -> p.sendTitle("Kapitel 3", "Der verrückte Professor", 10, 60, 10);
            case 4 -> {
                p.sendTitle("Kapitel 4", "Die Seelenwaffe erwacht", 10, 60, 10);
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
            }
            case 5 -> p.sendTitle("Kapitel 5", "Die ersten Admins", 10, 60, 10);
            case 6 -> p.sendTitle("Kapitel 6", "Der Adminpalast", 10, 60, 10);
            case 7 -> p.sendTitle("Kapitel 7", "Sanctum der Admins", 10, 60, 10);
            case 8 -> p.sendTitle("Kapitel 8", "Der Gelbe Admin kippt", 10, 60, 10);
            case 9 -> p.sendTitle("Kapitel 9", "Die mysteriöse Figur", 10, 60, 10);
            case 10 -> p.sendTitle("Kapitel 10", "Ritual der Hellen Materie", 10, 60, 10);
            case 11 -> p.sendTitle("Kapitel 11", "Schleier der Dunkelheit", 10, 60, 10);
            case 12 -> p.sendTitle("Kapitel 12", "Die Wahrheit", 10, 60, 10);
            case 13 -> p.sendTitle("Kapitel 13", "Der Gelbe Admin wird böse", 10, 60, 10);
            case 14 -> p.sendTitle("Kapitel 14", "Die Welt rüstet auf", 10, 60, 10);
            case 15 -> p.sendTitle("Kapitel 15", "Seelenwaffen erwachen", 10, 60, 10);
            case 16 -> p.sendTitle("Kapitel 16", "Ultimativer Commandblock", 10, 60, 10);
            case 17 -> p.sendTitle("Kapitel 17", "Sanctum bricht", 10, 60, 10);
            case 18 -> p.sendTitle("Kapitel 18", "Deine Verwandlung", 10, 60, 10);
            case 19 -> p.sendTitle("Kapitel 19", "Letzter Konflikt", 10, 60, 10);
            case 20 -> p.sendTitle("Kapitel 20", "ENTSCHEIDUNG", 10, 80, 10);

            default -> {
                p.sendMessage(ChatColor.GREEN + "Die Geschichte ist abgeschlossen.");
                return;
            }
        }

        p.setMetadata("chapter", new FixedMetadataValue(this, chapter + 1));
    }
}

