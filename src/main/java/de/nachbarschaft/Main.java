package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Phase 6 geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin deaktiviert");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        // ADMIN FORM
        if (cmd.getName().equalsIgnoreCase("adminform")) {
            p.sendTitle("§5Admin Erwachen", "§dDie Macht fließt durch dich...");
            p.getWorld().strikeLightningEffect(p.getLocation());
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 2));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            p.sendMessage("§dDu spürst die Macht der Admins...");
            return true;
        }

        // SANCTUM TELEPORT
        if (cmd.getName().equalsIgnoreCase("sanctum")) {
            Location sanctum = new Location(p.getWorld(), 0, 150, 0);
            p.teleport(sanctum);
            p.sendMessage("§5Du bist im Sanctum der Admins angekommen...");
            return true;
        }

        // SANCTUM WARNUNG
        if (cmd.getName().equalsIgnoreCase("sanctumwarn")) {
            Bukkit.broadcastMessage("§4⚠ Sanctum Warnung! Eine große Macht bewegt sich!");
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
            return true;
        }

        // RITUAL SYSTEM
        if (cmd.getName().equalsIgnoreCase("ritual")) {
            p.sendMessage("§cRituale müssen im Nether stattfinden!");
            p.sendMessage("§7Suche den Professor… er kennt den Weg.");
            p.playSound(p.getLocation(), Sound.AMBIENT_NETHER_WASTES_MOOD, 1, 1);
            return true;
        }

        // SEELEN SYSTEM START
        if (cmd.getName().equalsIgnoreCase("seelenstart")) {
            p.sendMessage("§bDeine Seele beginnt zu erwachen…");
            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
            return true;
        }

        // WAFFE
        if (cmd.getName().equalsIgnoreCase("waffe")) {

            ItemStack keyblade = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta meta = keyblade.getItemMeta();

            meta.setDisplayName(ChatColor.AQUA + "Seelenschlüssel");
            meta.setUnbreakable(true);
            keyblade.setItemMeta(meta);

            keyblade.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
            keyblade.addUnsafeEnchantment(Enchantment.MENDING, 1);

            p.getInventory().addItem(keyblade);
            p.sendMessage("§aDu hast deine Seelenwaffe erhalten!");
            return true;
        }

        // PRÜFUNG
        if (cmd.getName().equalsIgnoreCase("prüfung")) {
            p.sendMessage("§eDie Prüfung hat begonnen… sei bereit.");
            return true;
        }

        // ADMIN STORY
        if (cmd.getName().equalsIgnoreCase("adminstory")) {
            p.sendMessage("§dDer Weg der Admins ist gefährlich…");
            p.sendMessage("§7Der Gelbe Admin wankt. Der Weiße beobachtet.");
            return true;
        }

        // STADT CHECK
        if (cmd.getName().equalsIgnoreCase("stadtcheck")) {
            p.sendMessage("§aOberstadt: Stabil");
            p.sendMessage("§cUnterstadt: Gefahr registriert!");
            return true;
        }

        return true;
    }
}

