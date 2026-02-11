package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NpcManager {

    private static final String NPC_META = "np_npc";
    private static final String ADMIN_META = "np_admin";
    private static final String NPC_ID_META = "np_id";

    private final JavaPlugin plugin;
    private final Map<String, NpcProfile> npcProfiles = new HashMap<>();
    private BukkitTask chatTask;

    public NpcManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadProfiles();
    }

    public void spawnInitialNpcs(World world) {
        Location upper = ((Main) plugin).getLocationFromConfig("locations.upper", world.getSpawnLocation().clone().add(6, 0, 6));
        Location lower = ((Main) plugin).getLocationFromConfig("locations.lower", world.getSpawnLocation().clone().add(-6, -2, -6));
        List<NpcProfile> players = npcProfiles.values().stream()
                .filter(profile -> !profile.admin)
                .toList();
        int split = players.size() / 2;
        for (int i = 0; i < players.size(); i++) {
            Location spawn = i < split ? upper : lower;
            spawnNpc(players.get(i), spawn, false);
        }
    }

    public void spawnHintAdmins(World world) {
        spawnNpc(npcProfiles.get("lilaner_admin"), world.getSpawnLocation().clone().add(3, 0, 3), true);
        spawnNpc(npcProfiles.get("gruener_admin"), world.getSpawnLocation().clone().add(-3, 0, -3), true);
    }

    public void spawnYellowAdmin(World world) {
        spawnNpc(npcProfiles.get("gelber_admin"), world.getSpawnLocation().clone().add(0, 0, 4), true);
    }

    public void spawnSanctumAdmins(World world) {
        Location sanctum = ((Main) plugin).getLocationFromConfig("locations.sanctum", world.getSpawnLocation().clone().add(0, 12, 0));
        spawnNpc(npcProfiles.get("weisser_admin"), sanctum.clone().add(1, 0, 0), true);
        spawnNpc(npcProfiles.get("dunkelblauer_admin"), sanctum.clone().add(-1, 0, 0), true);
        spawnNpc(npcProfiles.get("hellblauer_admin"), sanctum.clone().add(0, 0, 1), true);
        spawnNpc(npcProfiles.get("orangener_admin"), sanctum.clone().add(0, 0, -1), true);
        spawnNpc(npcProfiles.get("pinker_admin"), sanctum.clone().add(2, 0, 1), true);
    }

    public void adminChatHint(Player p) {
        p.sendMessage(ChatColor.LIGHT_PURPLE + "[Lila] " + ChatColor.WHITE + "Vertraue nicht jedem Licht.");
        p.sendMessage(ChatColor.GREEN + "[Grün] " + ChatColor.WHITE + "Die Hinweise liegen im Sanctum verborgen.");
    }

    public void adminChatSoulStart(Player p) {
        p.sendMessage(ChatColor.YELLOW + "[Gelb] " + ChatColor.WHITE + "Deine Seelenwaffen reagieren auf deinen Mut.");
    }

    public void adminChatSanctumCall(Player p) {
        p.sendMessage(ChatColor.WHITE + "[Weiß] " + ChatColor.WHITE + "Ich rufe dich. Das Sanctum wartet.");
    }

    public void adminStoryBeat(Player p) {
        p.sendMessage("§7Die Admins sammeln sich. Ein Befehl liegt in der Luft...");
        adminChatHint(p);
        adminChatSanctumCall(p);
    }

    public String getCityStatus() {
        return "§eOberstadt: §a" + countNpcs(false, true) + " NPCs §7| Unterstadt: §a" + countNpcs(false, false)
                + " NPCs §7| Admins: §6" + countNpcs(true, false);
    }

    public void scheduleAmbientChat() {
        if (chatTask != null) {
            return;
        }
        chatTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            List<NpcProfile> speakers = new ArrayList<>();
            for (NpcProfile profile : npcProfiles.values()) {
                if (!profile.admin) {
                    speakers.add(profile);
                }
            }
            if (speakers.isEmpty()) {
                return;
            }
            Collections.shuffle(speakers);
            NpcProfile speaker = speakers.get(0);
            String line = speaker.dialogue.get((int) (Math.random() * speaker.dialogue.size()));
            Bukkit.broadcastMessage("§7<" + speaker.displayName + "> §f" + line);
        }, 20L * 15, 20L * 25);
    }

    private int countNpcs(boolean admin, boolean upper) {
        int count = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Villager villager : world.getEntitiesByClass(Villager.class)) {
                if (villager.hasMetadata(NPC_META) && villager.hasMetadata(ADMIN_META)) {
                    boolean isAdmin = villager.getMetadata(ADMIN_META).get(0).asBoolean();
                    if (isAdmin == admin) {
                        if (!admin) {
                            boolean isUpper = villager.getMetadata("np_upper").get(0).asBoolean();
                            if (isUpper == upper) {
                                count++;
                            }
                        } else {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private void spawnNpc(NpcProfile profile, Location location, boolean admin) {
        if (profile == null || location == null) {
            return;
        }
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        for (Villager villager : world.getEntitiesByClass(Villager.class)) {
            if (villager.hasMetadata(NPC_META)
                    && villager.hasMetadata(NPC_ID_META)
                    && villager.getMetadata(NPC_ID_META).get(0).asString().equals(profile.id)) {
                return;
            }
        }
        Villager villager = world.spawn(location, Villager.class);
        villager.setCustomNameVisible(true);
        villager.setCustomName(profile.displayName);
        villager.setAI(false);
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setRemoveWhenFarAway(false);
        villager.setMetadata(NPC_META, new FixedMetadataValue(plugin, true));
        villager.setMetadata(ADMIN_META, new FixedMetadataValue(plugin, admin));
        villager.setMetadata(NPC_ID_META, new FixedMetadataValue(plugin, profile.id));
        if (!admin) {
            boolean upper = location.getBlockY() >= ((Main) plugin).getLocationFromConfig("locations.upper", location).getBlockY();
            villager.setMetadata("np_upper", new FixedMetadataValue(plugin, upper));
        }
    }

    private void loadProfiles() {
        addProfile("gelber_admin", "§eGelber Admin", true, List.of("Die Seelenwaffen erwachen…", "Bewahre das Gleichgewicht."));
        addProfile("lilaner_admin", "§dLilaner Admin", true, List.of("Du wirst bald Antworten finden.", "Folge dem Schatten."));
        addProfile("gruener_admin", "§aGrüner Admin", true, List.of("Ich zeige dir den Weg.", "Die Oberstadt wird beobachten."));
        addProfile("hellblauer_admin", "§bHellblauer Admin", true, List.of("Das Sanctum ist ruhig.", "Noch nicht alle sind bereit."));
        addProfile("orangener_admin", "§6Orangener Admin", true, List.of("Feuer im Herzen, Fokus im Geist."));
        addProfile("pinker_admin", "§dPinker Admin", true, List.of("Vertraue deinem Instinkt."));
        addProfile("weisser_admin", "§fWeißer Admin", true, List.of("Ich rufe dich.", "Das Sanctum öffnet sich."));
        addProfile("dunkelblauer_admin", "§1Dunkelblauer Admin", true, List.of("Die Tiefe kennt deine Wahrheit."));

        addProfile("braaxic", "Braaxic", false, List.of("Habt ihr das gesehen?", "In der Unterstadt ist es lauter geworden."));
        addProfile("caandre", "cAANDRE", false, List.of("Die Oberstadt wirkt heute fremd.", "Ich halte mich an die Regeln."));
        addProfile("danergyhd", "DanergyHD", false, List.of("Wer hat den Alarm ausgelöst?", "Ich will nur nach Hause."));
        addProfile("debitorxp", "DebitorXP", false, List.of("Der Markt ist leer.", "Ich brauche Vorräte."));
        addProfile("einm4nu", "EinM4nu", false, List.of("Ich habe ein Geräusch gehört.", "Die Schatten werden länger."));
        addProfile("eiterbeule", "Eiterbeule", false, List.of("Ich bleibe bei den Laternen.", "Zu viel Nebel."));
        addProfile("gommehd", "GommeHD", false, List.of("Das ist kein normales Spiel mehr.", "Bleibt zusammen!"));
        addProfile("kiru039", "Kiru039", false, List.of("Die Unterstadt flüstert.", "Ich vertraue den Admins nicht."));
        addProfile("notgambo", "notGambo", false, List.of("Habt ihr Hunger?", "Ich teile meine Rationen."));
        addProfile("odumano", "Odumano", false, List.of("Ich zähle die Ausgänge.", "Wir brauchen einen Plan."));
        addProfile("ojicristos", "OjiCristos", false, List.of("Hoffnung gibt es immer.", "Ich sehe ein Licht."));
        addProfile("ryney", "ryney", false, List.of("Ich bleibe in der Oberstadt.", "Keiner geht allein."));
        addProfile("snowfeather25", "Snowfeather25", false, List.of("Das Wetter kippt.", "Ich höre Schritte."));
        addProfile("tridanyt", "TriDanYT", false, List.of("Ich halte Wache.", "Die Türen sind zu."));
        addProfile("yamiyami", "YamiYami_", false, List.of("Ich teste die Grenzen.", "Noch bin ich frei."));
        addProfile("zitachy", "Zitachy_Toji", false, List.of("Die Zeichen stimmen.", "Wir sollten weiterziehen."));
        addProfile("amir", "Amir", false, List.of("Ich sehe die Admins.", "Warum sind sie so spät?"));
        addProfile("awesomeelina", "AwesomeElina", false, List.of("Ich sammle Hinweise.", "Irgendwas stimmt hier nicht."));
        addProfile("blackchecktv", "BlackCheckTV", false, List.of("Ich höre die Sirenen.", "Bleibt ruhig."));
        addProfile("brostastic", "Brostastic", false, List.of("Wir sind nicht allein.", "Ich sehe Schatten."));
        addProfile("deadston3", "deadston3", false, List.of("Wir müssen runter.", "Die Unterstadt ist sicherer."));
        addProfile("ioente", "IOEnte", false, List.of("Ich schütze die Gruppe.", "Keine Panik."));
        addProfile("kiyanesh", "Kiyanesh", false, List.of("Der Weg ist blockiert.", "Wir brauchen Hilfe."));
        addProfile("livelucyt", "LiveLucYT", false, List.of("Ich filme alles.", "Das ist wichtig."));
        addProfile("lukeucraft", "LukeUCraft", false, List.of("Die Luft riecht seltsam.", "Ich traue dem Nebel nicht."));
        addProfile("platiumdot", "PlatiumDot", false, List.of("Ich wachse daran.", "Wir schaffen das."));
        addProfile("professorpopelyt", "ProfessorPopelYT", false, List.of("Ich beobachte jede Bewegung.", "Forschung ist alles."));
        addProfile("saschq", "Saschq", false, List.of("Ich bleibe wach.", "Die Nacht ist lang."));
        addProfile("schmockyyy", "schmockyyy", false, List.of("Ein Schritt zurück.", "Lasst uns leise sein."));
        addProfile("tobyte", "Tobyte", false, List.of("Ich zähle mit.", "Nicht trennen."));
        addProfile("lelsnoopy", "Lelsnoopy", false, List.of("Ich vertraue dir.", "Wir müssen handeln."));
        addProfile("syoouuu", "SYoouuu", false, List.of("Die Lichter flackern.", "Ich hab Angst."));
        addProfile("krustie", "Krustie", false, List.of("Das wird hart.", "Aber wir sind bereit."));
        addProfile("kiyanes", "Kiyanes", false, List.of("Ich sichere die Wege.", "Folgt mir."));
    }

    private void addProfile(String id, String displayName, boolean admin, List<String> dialogue) {
        npcProfiles.put(id, new NpcProfile(id, displayName, admin, dialogue));
    }

    private static class NpcProfile {
        private final String id;
        private final String displayName;
        private final boolean admin;
        private final List<String> dialogue;

        private NpcProfile(String id, String displayName, boolean admin, List<String> dialogue) {
            this.id = id;
            this.displayName = displayName;
            this.admin = admin;
            this.dialogue = dialogue;
        }
    }
}
