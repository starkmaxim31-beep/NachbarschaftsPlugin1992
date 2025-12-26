package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Nachbarschaft] Phase 6 aktiv!");
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("adminform").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;

            p.sendMessage(ChatColor.AQUA + "⚡ Du spürst eine riesige Macht in dir erwachen...");
            p.getWorld().strikeLightningEffect(p.getLocation());
            p.getWorld().strikeLightningEffect(p.getLocation());

            p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 999999, 3));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 999999, 2));

            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage(ChatColor.GOLD + "Du bist nun in deiner Admin-Form!");
            return true;
        });

        getCommand("waffe").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;

            ItemStack keyblade = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta meta = keyblade.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "◆ Seelenwaffe – Schlüsselschwert ◆");
            meta.setUnbreakable(true);
            keyblade.setItemMeta(meta);

            keyblade.addUnsafeEnchantment(Enchantment.SHARPNESS, 6);
            keyblade.addUnsafeEnchantment(Enchantment.MENDING, 1);

            p.getInventory().addItem(keyblade);
            p.sendMessage(ChatColor.LIGHT_PURPLE + "✨ Deine Seelenwaffe hat dich gefunden!");
            return true;
        });

        getCommand("seelenstart").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;

            p.sendMessage(ChatColor.GREEN + "💚 Deine Seele hat ihre Reise begonnen…");
            p.sendMessage(ChatColor.GRAY + "Weitere Kräfte folgen in den nächsten Kapiteln.");
            return true;
        });

        getCommand("adminstory").setExecutor((sender, cmd, label, args) -> {
            sender.sendMessage(ChatColor.YELLOW + "📖 Die Admin Story beginnt...");
            sender.sendMessage(ChatColor.GRAY + "Es gibt gute Admins… und welche, die gefallen sind.");
            return true;
        });

        getCommand("stadtcheck").setExecutor((sender, cmd, label, args) -> {
            sender.sendMessage(ChatColor.BLUE + "🏙 Oberstadt & Unterstadt sind vorbereitet.");
            sender.sendMessage(ChatColor.GRAY + "Kapitel bestimmen ihren aktiven Zustand.");
            return true;
        });

        getCommand("sanctumwarn").setExecutor((sender, cmd, label, args) -> {
            sender.sendMessage(ChatColor.DARK_RED + "⚠ Betrete das Sanctum nur, wenn du bereit bist.");
            return true;
        });

        getCommand("ritual").setExecutor((sender, cmd, label, args) -> {
            sender.sendMessage(ChatColor.DARK_PURPLE + "🔥 Die Rituale finden im Nether statt.");
            sender.sendMessage(ChatColor.GRAY + "Nur Mutige wagen es…");
            return true;
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Nachbarschaft] Plugin deaktiviert");
    }
}

