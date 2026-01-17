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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> chapterProgress = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Nachbarschaft Plugin aktiv (NUMMER 1)");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    /* =========================
       KAPITEL COMMAND
       ========================= */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;

        if (cmd.getName().equalsIgnoreCase("kapitel")) {
            int chapter = chapterProgress.getOrDefault(p.getUniqueId(), 1);
            p.sendMessage(ChatColor.GOLD + getChapterText(chapter));
            chapterProgress.put(p.getUniqueId(), chapter + 1);
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("waffe")) {
            giveSoulBlade(p);
            return true;
        }

        return true;
    }

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

    /* =========================
       SEELENKLINGE
       ========================= */
    private void giveSoulBlade(Player p) {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "◆ Seelenklinge ◆");
        meta.setLore(List.of(
                ChatColor.GRAY + "Gebunden an: " + p.getName(),
                ChatColor.DARK_PURPLE + "Eine Waffe mit eigenem Willen"
        ));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);
        p.getInventory().addItem(sword);
        p.sendMessage(ChatColor.GREEN + "Deine Seelenklinge erwacht.");
    }

    private boolean isSoulBlade(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return ChatColor.stripColor(item.getItemMeta().getDisplayName())
                .equalsIgnoreCase("◆ Seelenklinge ◆");
    }

    /* =========================
       FÄHIGKEITEN (EVENTS)
       ========================= */
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!isSoulBlade(p.getInventory().getItemInMainHand())) return;

        // DASH – Rechtsklick, nur am Boden
        if (e.getAction().toString().contains("RIGHT_CLICK") && !p.isSneaking()) {
            if (!p.isOnGround()) return;

            Vector dir = p.getLocation().getDirection().normalize().multiply(1.8);
            p.setVelocity(dir);

            for (Entity ent : p.getNearbyEntities(2, 2, 2)) {
                if (ent instanceof LivingEntity le && ent != p) {
                    le.damage(6, p);
                }
            }
        }

        // LUFTANGRIFF – Shift + Rechtsklick
        if (e.getAction().toString().contains("RIGHT_CLICK") && p.isSneaking()) {
            for (Entity ent : p.getNearbyEntities(4, 3, 4)) {
                if (ent instanceof LivingEntity le && ent != p) {
                    le.setVelocity(new Vector(0, 1.2, 0));
                    le.damage(8, p);
                }
            }
        }
    }
}

