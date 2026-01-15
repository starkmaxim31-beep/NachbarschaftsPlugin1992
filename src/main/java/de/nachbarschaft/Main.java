package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    // ================= COMMANDS =================

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

            case "adminpalast" -> {
                p.teleport(new Location(w, 0, 120, 0));
                p.sendMessage(ChatColor.YELLOW + "Willkommen im Admin Palast!");
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

            case "waffe" -> giveSoulSword(p);

            case "seelenbogen" -> giveSoulBow(p);

            case "prüfung" -> {
                p.sendTitle(ChatColor.RED + "PRÜFUNG", ChatColor.GRAY + "Beweise deine Stärke", 10, 70, 20);
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 300, 1));
            }

            case "kapitel" -> handleChapter(p);

            case "adminhelp" -> {
                p.sendMessage(ChatColor.YELLOW + "==== ADMIN HILFE ====");
                p.sendMessage("/sanctum");
                p.sendMessage("/adminpalast");
                p.sendMessage("/seelenstart");
                p.sendMessage("/waffe");
                p.sendMessage("/seelenbogen");
                p.sendMessage("/prüfung");
                p.sendMessage("/kapitel");
            }
        }
        return true;
    }

    // ================= SEELENWAFFEN =================

    private void giveSoulSword(Player p) {
        if (p.hasMetadata("soulSword")) {
            p.sendMessage(ChatColor.RED + "Du besitzt diese Waffe bereits.");
            return;
        }

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "◆ Seelenklinge ◆");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Gebunden an: " + p.getName(),
                ChatColor.DARK_PURPLE + "Dash • Multi-Hit • Luftangriff"
        ));
        meta.setUnbreakable(true);

        sword.setItemMeta(meta);
        p.getInventory().addItem(sword);
        p.setMetadata("soulSword", new FixedMetadataValue(this, true));

        p.sendMessage(ChatColor.GREEN + "✔ Die Seelenklinge akzeptiert dich.");
    }

    private void giveSoulBow(Player p) {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "✦ Seelenbogen ✦");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Gebunden an: " + p.getName(),
                ChatColor.DARK_PURPLE + "Pfeilregen • Flug • Superskill"
        ));
        meta.setUnbreakable(true);

        bow.setItemMeta(meta);
        p.getInventory().addItem(bow);

        p.sendMessage(ChatColor.AQUA + "🏹 Der Seelenbogen erwacht...");
    }

    // ================= EVENTS =================

    // DASH + LUFTANGRIFF
    @EventHandler
    public void onSwordUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item.getType() != Material.NETHERITE_SWORD) return;
        if (!item.getItemMeta().getDisplayName().contains("Seelenklinge")) return;

        Vector v = p.getLocation().getDirection().multiply(1.4);
        p.setVelocity(v);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    // MULTI HIT
    @EventHandler
    public void onMultiHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;

        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType() != Material.NETHERITE_SWORD) return;
        if (!item.getItemMeta().getDisplayName().contains("Seelenklinge")) return;

        e.setDamage(e.getDamage() * 3);
    }

    // PFEILREGEN
    @EventHandler
    public void onBowSkill(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!p.isSneaking()) return;
        if (item.getType() != Material.BOW) return;
        if (!item.getItemMeta().getDisplayName().contains("Seelenbogen")) return;

        Location target = p.getTargetBlock(null, 30).getLocation();

        for (int i = 0; i < 25; i++) {
            Arrow arrow = target.getWorld().spawnArrow(
                    target.clone().add(Math.random()*6-3, 10, Math.random()*6-3),
                    new Vector(0, -1, 0),
                    1.5f, 0
            );
            arrow.setShooter(p);
        }

        p.sendMessage(ChatColor.DARK_PURPLE + "☄ Pfeilregen entfesselt!");

        // FLUG 3 SEKUNDEN
        p.setAllowFlight(true);
        p.setFlying(true);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            p.setFlying(false);
            p.setAllowFlight(false);
        }, 60L);
    }

    // ================= STORY =================

    private void handleChapter(Player p) {
        int chapter = p.hasMetadata("chapter")
                ? p.getMetadata("chapter").get(0).asInt()
                : 1;

        p.sendMessage(ChatColor.GOLD + "📖 Kapitel " + chapter);
        p.setMetadata("chapter", new FixedMetadataValue(this, chapter + 1));
    }
}

