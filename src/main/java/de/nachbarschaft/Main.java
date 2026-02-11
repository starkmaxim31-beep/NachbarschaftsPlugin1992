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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> chapterProgress = new HashMap<>();
    private NpcManager npcManager;
    private VoiceCommandManager voiceCommandManager;
    private RuleManager ruleManager;

    /* ===================== ENABLE ===================== */

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        ensureLocationDefaults();
        npcManager = new NpcManager(this);
        voiceCommandManager = new VoiceCommandManager(this);
        ruleManager = new RuleManager(this);
        getLogger().info("Nachbarschaft Plugin aktiv!");
    }

    @Override
    public void onDisable() {
        if (voiceCommandManager != null) {
            voiceCommandManager.stopAllRepeats();
        }
        getLogger().info("Nachbarschaft Plugin deaktiviert!");
    }

    /* ===================== COMMANDS ===================== */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        switch (cmd.getName().toLowerCase()) {

            case "kapitel" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                int chapter = chapterProgress.getOrDefault(p.getUniqueId(), 1);
                p.sendMessage(ChatColor.GOLD + getChapterText(chapter));
                if (chapter == 4) {
                    npcManager.spawnHintAdmins(p.getWorld());
                    npcManager.adminChatHint(p);
                }
                chapterProgress.put(p.getUniqueId(), chapter + 1);
            }

            case "waffe" -> giveSoulWeapons(p);
            case "waffe" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                giveSoulWeapons(p);
            }

            case "seelenstart" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.sendMessage("§bDu spürst die Seelenenergie…");
                giveSoulWeapons(p);
                npcManager.spawnYellowAdmin(p.getWorld());
                npcManager.adminChatSoulStart(p);
            }

            case "sanctum" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.teleport(getLocationFromConfig("locations.sanctum", p.getWorld().getSpawnLocation().clone().add(0, 10, 0)));
                npcManager.spawnSanctumAdmins(p.getWorld());
                npcManager.adminChatSanctumCall(p);
            }

            case "sanctumwarn" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.sendMessage("§6Eine kalte Stimme warnt dich: Das Sanctum ist instabil!");
            }

            case "stadtcheck" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.sendMessage(npcManager.getCityStatus());
            }

            case "adminpalast" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.teleport(getLocationFromConfig("locations.adminpalast", p.getWorld().getSpawnLocation().clone().add(0, 5, 0)));
            }

            case "adminform" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.sendMessage("§5Eine Admin-Form legt sich über dich…");
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 20, 0));
            }

            case "prüfung" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.sendMessage("§6Die Prüfung beginnt. Bewahre die Ruhe.");
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
            }

            case "adminstory" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                npcManager.adminStoryBeat(p);
            }

            case "voicecmd" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                if (args.length == 0) {
                    p.sendMessage("§e/voicecmd <phrase>");
                } else {
                    String phrase = String.join(" ", args);
                    if (!voiceCommandManager.triggerPhrase(p, phrase)) {
                        p.sendMessage("§7Kein Voice-Befehl gefunden: §f" + phrase);
                    }
                }
            }

            case "regel", "rules" -> ruleManager.handleRuleCommand(sender, args);

            case "adminhelp" -> {
                Player p = getPlayerOrWarn(sender);
                if (p == null) return true;
                p.sendMessage("§e/kapitel §7Story-Fortschritt");
                p.sendMessage("§e/waffe §7Seelenwaffen erhalten");
                p.sendMessage("§e/seelenstart §7Seelenpfad starten");
                p.sendMessage("§e/sanctum §7Teleport ins Sanctum");
                p.sendMessage("§e/sanctumwarn §7Warnung");
                p.sendMessage("§e/stadtcheck §7Stadtstatus");
                p.sendMessage("§e/adminpalast §7Admin Palast");
                p.sendMessage("§e/adminform §7Admin-Kraft");
                p.sendMessage("§e/prüfung §7Prüfung starten");
                p.sendMessage("§e/adminstory §7Admin Story-Beat");
                p.sendMessage("§e/voicecmd §7Voice-Befehle auslösen");
                p.sendMessage("§e/regel §7Regeln anzeigen");
            }

            default -> sender.sendMessage("§7Unbekannter Befehl.");
        }
        return true;
    }

    private Player getPlayerOrWarn(CommandSender sender) {
        if (sender instanceof Player player) {
            return player;
        }
        sender.sendMessage("§7Dieser Befehl ist nur für Spieler.");
        return null;
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
            p.sendMessage("§7Du besitzt deine Seelenwaffen bereits.");
            return;
        }

        // SEELENKLINGE
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta sm = sword.getItemMeta();
        sm.setDisplayName("§b◆ Seelenklinge ◆");
        sm.setLore(List.of("§7Gebunden an: " + p.getName()));
        sm.setDisplayName("§b◆ Seelenschwert ◆");
        sm.setLore(List.of(
                "§7Gebunden an: " + p.getName(),
                "§bFähigkeit: §fSeelensprint",
                "§bFähigkeit: §fKlingenstoß"
        ));
        sm.setUnbreakable(true);
        sword.setItemMeta(sm);

        // SEELENBOGEN
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bm = bow.getItemMeta();
        bm.setDisplayName("§d◆ Seelenbogen ◆");
        bm.setLore(List.of(
                "§7Gebunden an: " + p.getName(),
                "§dFähigkeit: §fSeelensalve",
                "§dFähigkeit: §fSplitterregen"
        ));
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
                .equalsIgnoreCase("◆ Seelenschwert ◆");
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
@@ -163,28 +284,71 @@ public class Main extends JavaPlugin implements Listener {
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        npcManager.spawnInitialNpcs(event.getPlayer().getWorld());
        npcManager.scheduleAmbientChat();
    }

    private void ensureLocationDefaults() {
        if (Bukkit.getWorlds().isEmpty()) {
            return;
        }
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        FileConfiguration cfg = getConfig();
        cfg.addDefault("locations.upper.world", spawn.getWorld().getName());
        cfg.addDefault("locations.upper.x", spawn.getX() + 6);
        cfg.addDefault("locations.upper.y", spawn.getY());
        cfg.addDefault("locations.upper.z", spawn.getZ() + 6);
        cfg.addDefault("locations.lower.world", spawn.getWorld().getName());
        cfg.addDefault("locations.lower.x", spawn.getX() - 6);
        cfg.addDefault("locations.lower.y", Math.max(1, spawn.getY() - 6));
        cfg.addDefault("locations.lower.z", spawn.getZ() - 6);
        cfg.addDefault("locations.sanctum.world", spawn.getWorld().getName());
        cfg.addDefault("locations.sanctum.x", spawn.getX());
        cfg.addDefault("locations.sanctum.y", spawn.getY() + 12);
        cfg.addDefault("locations.sanctum.z", spawn.getZ());
        cfg.addDefault("locations.adminpalast.world", spawn.getWorld().getName());
        cfg.addDefault("locations.adminpalast.x", spawn.getX());
        cfg.addDefault("locations.adminpalast.y", spawn.getY() + 6);
        cfg.addDefault("locations.adminpalast.z", spawn.getZ() + 4);
        cfg.options().copyDefaults(true);
        saveConfig();
    }

    public Location getLocationFromConfig(String path, Location fallback) {
        FileConfiguration cfg = getConfig();
        String worldName = cfg.getString(path + ".world", fallback.getWorld().getName());
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            world = fallback.getWorld();
        }
        double x = cfg.getDouble(path + ".x", fallback.getX());
        double y = cfg.getDouble(path + ".y", fallback.getY());
        double z = cfg.getDouble(path + ".z", fallback.getZ());
        return new Location(world, x, y, z);
    }
}



