package de.nachbarschaft;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Phase 5 aktiviert!");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin deaktiviert.");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können das benutzen!");
            return true;
        }

        Player p = (Player) sender;


        // -----------------------
        // SEELENWAFFEN SYSTEM
        // -----------------------
        if(cmd.getName().equalsIgnoreCase("seelenstart")){

            Bukkit.broadcastMessage("§b✦ Die Energie der Seelen erwacht...");
            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.playSound(pl.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                pl.spawnParticle(Particle.SOUL, pl.getLocation(), 100);
            }
            return true;
        }


        // -----------------------
        // SCHLÜSSELSCHWERT
        // -----------------------
        if(cmd.getName().equalsIgnoreCase("waffe")){

            ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta meta = sword.getItemMeta();
            meta.setDisplayName("§6Schlüsselschwert");
            sword.setItemMeta(meta);

            p.getInventory().addItem(sword);

            p.sendMessage("§6Du hast deine Seelenwaffe erhalten.");
            return true;
        }


        // -----------------------
        // SANCTUM WARN EVENT
        // -----------------------
        if(cmd.getName().equalsIgnoreCase("sanctumwarn")){

            Bukkit.broadcastMessage("§c⚠ Das Sanctum reagiert... etwas stimmt nicht!");

            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 1));
                pl.playSound(pl.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 1, 1);
            }

            return true;
        }


        // -----------------------
        // PRÜFUNGEN
        // -----------------------
        if(cmd.getName().equalsIgnoreCase("prüfung")){

            p.sendTitle("§6Prüfung beginnt", "§fHalte durch...",10,100,10);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 3));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));

            return true;
        }


        // -----------------------
        // FINAL ARC
        // -----------------------
        if(cmd.getName().equalsIgnoreCase("adminfinal")){

            Bukkit.broadcastMessage("§e[Weißer Admin] Es ist Zeit...");
            Bukkit.broadcastMessage("§6[Gelber Admin] Ich… ich kontrolliere das hier!");

            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,1,1);
                pl.spawnParticle(Particle.DRAGON_BREATH, pl.getLocation(), 200);
            }

            return true;
        }

        return false;
    }
}

