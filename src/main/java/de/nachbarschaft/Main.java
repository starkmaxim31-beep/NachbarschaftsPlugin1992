package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> chapterProgress = new HashMap<>();

    /* ===================== ENABLE ===================== */

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Nachbarschaft Plugin aktiv!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nachbarschaft Plugin deaktiviert!");
    }

    /* ===================== COMMANDS ===================== */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        switch (cmd.getName().toLowerCase()) {

            case "kapitel" -> {
                int chapter = chapterProgress.getOrDefault(p.getUniqueId(), 1);
                p.sendMessage(ChatColor.GOLD + getChapterText(chapter));
                chapterProgress.put(p.getUniqueId(), chapter + 1);
            }

            case "waffe" -> giveSoulWeapons(p);

            case "adminhelp" -> {
                p.sendMessage("§e/kapitel §7Story-Fortschritt");
                p.sendMessage("§e/waffe §7Seelenwaffen erhalten");
            }
        }
        return true;
    }

    /* ===================== KAPITEL ===================== */

    private String getChapterText(int c) {
        return switch (c) {
            case 1 -> "📖 Kapitel 1 – Ankunft in der Oberstadt";
            case 2 -> "📖 Kapitel 2 – Die Unterstadt";
            case 3 -> "📖 Kapitel 3 – Der verrückte Professor";
            case 4 -> "📖 Kapitel 4 – Die Seelenwaffe erwacht";
            case 5 -> "📖 Kapitel 5 – Die ersten Admins";
            case 6 -> "📖 Kapitel 6 – Der Admin Palast";
            case 7 -> "📖 Kapitel 7 – Sanctum der Admins";
            case 8 -> "📖 Kapitel 8 – Der Gelbe Admin beginnt zu fallen";
            case 9 -> "📖 Kapitel 9 – Die mysteriöse Spielfigur";
            case 10 -> "📖 Kapitel 10 – Ritual der hellen Materie";
            case 11 -> "📖 Kapitel 11 – Ritual des Schleiers";
            case 12 -> "📖 Kapitel 12 – Wahrheit über Lila & Grün";
            case 13 -> "📖 Kapitel 13 – Der Gelbe Admin kippt";
            case 14 -> "📖 Kapitel 14 – NPCs werden Krieger";
            case 15 -> "📖 Kapitel 15 – Wahrheit über Adminstruktur";
            case 16 -> "📖 Kapitel 16 – Der ultimative Commandblock";
            case 17 -> "📖 Kapitel 17 – Das Sanctum bricht";
            case 18 -> "📖 Kapitel 18 – Deine Verwandlung";
            case 19 -> "📖 Kapitel 19 – Der letzte Konflikt";
            case 20 -> "🔥 Kapitel 20 – Die Entscheidung";
            default -> "🎉 Du hast alle Kapitel abgeschlossen.";
        };
    }

    /* ===================== SEELENWAFFEN ===================== */

    private void giveSoulWeapons(Player p) {

        if (p.hasMetadata("soulWeaponGiven")) {
            p.sendMessage("§cDu besitzt deine Seelenwaffen bereits.");
            return;
        }

        // SEELENKLINGE
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta sm = sword.getItemMeta();
        sm.setDisplayName("§b◆ Seelenklinge ◆");
        sm.setLore(List.of("§7Gebunden an: " + p.getName()));
        sm.setUnbreakable(true);
        sword.setItemMeta(sm);

        // SEELENBOGEN
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bm = bow.getItemMeta();
        bm.setDisplayName("§d◆ Seelenbogen ◆");
        bm.setUnbreakable(true);
        bow.setItemMeta(bm);

        p.getInventory().addItem(sword, bow);
        p.setMetadata("soulWeaponGiven", new FixedMetadataValue(this, true));

        p.sendMessage("§aDeine Seelenwaffen erwachen.");
    }

    private boolean isSoulBlade(ItemStack item) {
        return item != null && item.hasItemMeta()
                && ChatColor.stripColor(item.getItemMeta().getDisplayName())
                .equalsIgnoreCase("◆ Seelenklinge ◆");
    }

    /* ===================== SEELENKLINGE – DASH ===================== */

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        if (!e.getAction().toString().contains("RIGHT")) return;
        Player p = e.getPlayer();

        if (!isSoulBlade(p.getInventory().getItemInMainHand())) return;

        Vector dir = p.getLocation().getDirection().normalize().multiply(1.7);
        p.setVelocity(dir);

        p.getWorld().spawnParticle(Particle.SOUL, p.getLocation(), 20);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        for (Entity ent : p.getNearbyEntities(2, 2, 2)) {
            if (ent instanceof LivingEntity le && ent != p) {
                le.damage(6, p);
                le.setVelocity(new Vector(0, 0.6, 0));
            }
        }
    }

    /* ===================== LUFTANGRIFF ===================== */

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {

        if (!e.isSneaking()) return;
        Player p = e.getPlayer();

        if (!isSoulBlade(p.getInventory().getItemInMainHand())) return;

        for (Entity ent : p.getNearbyEntities(4, 4, 4)) {
            if (ent instanceof LivingEntity le && ent != p) {
                le.setVelocity(new Vector(0, 1.2, 0));
                le.damage(8, p);
            }
        }
        p.getWorld().spawnParticle(Particle.EXPLOSION, p.getLocation(), 1);
    }

    /* ===================== SEELENBOGEN ===================== */

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {

        if (!(e.getEntity() instanceof Player p)) return;
        ItemStack bow = e.getBow();
        if (bow == null || !bow.hasItemMeta()) return;

        if (!bow.getItemMeta().getDisplayName().contains("Seelenbogen")) return;

        for (int i = 0; i < 6; i++) {
            Arrow a = p.launchProjectile(Arrow.class);
            a.setVelocity(new Vector(
                    Math.random() - 0.5,
                    -0.2,
                    Math.random() - 0.5
            ));
            a.setDamage(4);
        }
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);
    }
}


