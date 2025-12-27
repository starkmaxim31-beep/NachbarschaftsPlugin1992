package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private final String BIND_TAG = "Seelenwaffe";

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

            case "kapitel":
                p.sendMessage(ChatColor.GOLD + "Kapitel System gestartet!");
                // hier hängst du später dein echtes System rein
                return true;

            case "sanctum":
                p.teleport(new Location(w, 200, 100, 200));
                p.sendMessage(ChatColor.DARK_PURPLE + "Du betrittst das Sanctum der Admins...");
                return true;

            case "sanctumwarn":
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "⚠ Ein Admin spürt Unruhe... Das Sanctum reagiert!");
                return true;

            case "stadtcheck":
                p.sendMessage(ChatColor.GREEN + "Oberstadt: X=100 Y=80 Z=100");
                p.sendMessage(ChatColor.GRAY + "Unterstadt: X=-100 Y=40 Z=-100");
                return true;

            case "adminpalast":
                p.teleport(new Location(w, 0, 120, 0));
                p.sendMessage(ChatColor.YELLOW + "Willkommen im Admin Palast!");
                return true;

            case "adminform":
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 999999, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));
                p.sendMessage(ChatColor.YELLOW + "Deine Adminkraft erwacht!");
                return true;

            case "waffe":
                giveSoulWeapon(p);
                return true;

            case "seelenstart":
                p.sendMessage(ChatColor.AQUA + "Deine Seele beginnt zu erwachen...");
                giveSoulWeapon(p);
                return true;

            case "prüfung":
                p.sendMessage(ChatColor.BLUE + "Die Prüfung beginnt...");
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                return true;

            case "adminstory":
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Die Geschichte der Admins entfaltet sich...");
                return true;
        }

        return false;
    }

    // ===========================
    //  SEELENWAFFE SYSTEM
    // ===========================
    private void giveSoulWeapon(Player p) {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Schlüsselschwert");
        meta.setLore(Collections.singletonList(BIND_TAG + ":" + p.getUniqueId()));

        meta.addEnchant(Enchantment.SHARPNESS, 5, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);

        meta.setUnbreakable(true);

        sword.setItemMeta(meta);
        p.getInventory().addItem(sword);

        p.sendMessage(ChatColor.GREEN + "Deine Seelenwaffe wurde dir gebunden!");
    }

    // Spieler kann sie NICHT droppen
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isBoundWeapon(e.getItemDrop().getItemStack(), e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Diese Waffe gehört deiner Seele!");
        }
    }

    // Spieler kann sie NICHT weitergeben
    @EventHandler
    public void onMove(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();

        if (isBoundWeapon(e.getCurrentItem(), p.getUniqueId())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Diese Waffe ist an dich gebunden!");
        }
    }

    private boolean isBoundWeapon(ItemStack item, UUID owner) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (!item.getItemMeta().hasLore()) return false;

        return item.getItemMeta().getLore().get(0).equals(BIND_TAG + ":" + owner);
    }
}



