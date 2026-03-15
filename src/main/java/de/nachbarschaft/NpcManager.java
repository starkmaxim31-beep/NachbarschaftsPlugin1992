package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class NpcManager {

    private static final String NPC_META = "np_npc";
    private static final String ADMIN_META = "np_admin";
    private static final String NPC_ID_META = "np_id";

    private final JavaPlugin plugin;
    private final Map<String, NpcProfile> npcProfiles = new HashMap<>();
    private final List<Villager> activeNpcs = new ArrayList<>();
    private NpcAI npcAI;

    public NpcManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadProfiles();
    }

    // Spawn alle NPCs
    public void spawnAllNpcs(World world) {
        Location spawn = world.getSpawnLocation();

        for (NpcProfile profile : npcProfiles.values()) {
            Location loc = spawn.clone().add(randomOffset(), 0, randomOffset());
            spawnNpc(profile, loc);
        }
    }

    public void startAI() {
        npcAI = new NpcAI(plugin, activeNpcs);
        npcAI.startAI();
    }

    private void spawnNpc(NpcProfile profile, Location location) {
        if (profile == null || location == null || location.getWorld() == null) return;

        World world = location.getWorld();

        // Prüfen ob NPC schon existiert
        for (Villager v : world.getEntitiesByClass(Villager.class)) {
            if (v.hasMetadata(NPC_ID_META) &&
                v.getMetadata(NPC_ID_META).get(0).asString().equals(profile.id)) return;
        }

        Villager villager = world.spawn(location, Villager.class);
        villager.setCustomName(profile.displayName);
        villager.setCustomNameVisible(true);
        villager.setAI(true);
        villager.setInvulnerable(true);
        villager.setRemoveWhenFarAway(false);

        villager.setMetadata(NPC_META, new FixedMetadataValue(plugin, true));
        villager.setMetadata(ADMIN_META, new FixedMetadataValue(plugin, profile.admin));
        villager.setMetadata(NPC_ID_META, new FixedMetadataValue(plugin, profile.id));

        activeNpcs.add(villager);
    }

    private double randomOffset() {
        return (Math.random() - 0.5) * 8;
    }

    private void loadProfiles() {
        // Admins
        addProfile("gelber_admin","§eGelber Admin",true);
        addProfile("lilaner_admin","§dLilaner Admin",true);
        addProfile("gruener_admin","§aGrüner Admin",true);
        addProfile("hellblauer_admin","§bHellblauer Admin",true);
        addProfile("orangener_admin","§6Orangener Admin",true);
        addProfile("pinker_admin","§dPinker Admin",true);
        addProfile("weisser_admin","§fWeißer Admin",true);
        addProfile("dunkelblauer_admin","§1Dunkelblauer Admin",true);

        // Spieler / NPCs
        addProfile("braaxic","Braaxic",false);
        addProfile("caandre","cAANDRE",false);
        addProfile("danergyhd","DanergyHD",false);
        addProfile("debitorxp","DebitorXP",false);
        addProfile("einm4nu","EinM4nu",false);
        addProfile("eiterbeule","Eiterbeule",false);
        addProfile("gommehd","GommeHD",false);
        addProfile("kiru039","Kiru039",false);
        addProfile("notgambo","notGambo",false);
        addProfile("odumano","Odumano",false);
        addProfile("ojicristos","OjiCristos",false);
        addProfile("ryney","ryney",false);
        addProfile("snowfeather25","Snowfeather25",false);
        addProfile("tridanyt","TriDanYT",false);
        addProfile("yamiyami","YamiYami_",false);
        addProfile("zitachy","Zitachy_Toji",false);
        addProfile("amir","Amir",false);
        addProfile("awesomeelina","AwesomeElina",false);
        addProfile("blackchecktv","BlackCheckTV",false);
        addProfile("brostastic","Brostastic",false);
        addProfile("deadston3","deadston3",false);
        addProfile("ioente","IOEnte",false);
        addProfile("kiyanesh","Kiyanesh",false);
        addProfile("livelucyt","LiveLucYT",false);
        addProfile("lukeucraft","LukeUCraft",false);
        addProfile("platiumdot","PlatiumDot",false);
        addProfile("professorpopelyt","ProfessorPopelYT",false);
        addProfile("saschq","Saschq",false);
        addProfile("schmockyyy","schmockyyy",false);
        addProfile("tobyte","Tobyte",false);
        addProfile("lelsnoopy","Lelsnoopy",false);
        addProfile("syoouuu","SYoouuu",false);
        addProfile("krustie","Krustie",false);
    }

    private void addProfile(String id,String name,boolean admin){
        npcProfiles.put(id,new NpcProfile(id,name,admin));
    }

    private static class NpcProfile {
        private final String id;
        private final String displayName;
        private final boolean admin;

        public NpcProfile(String id,String displayName,boolean admin){
            this.id=id;
            this.displayName=displayName;
            this.admin=admin;
        }
    }

    public List<Villager> getActiveNpcs() {
        return activeNpcs;
    }
}
