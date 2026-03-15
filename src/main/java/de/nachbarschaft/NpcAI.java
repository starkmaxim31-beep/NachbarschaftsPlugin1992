package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NpcAI {

    private final JavaPlugin plugin;
    private final List<Villager> npcs;
    private final Random random = new Random();

    private BukkitTask movementTask;
    private BukkitTask conversationTask;

    public NpcAI(JavaPlugin plugin,List<Villager> npcs){
        this.plugin = plugin;
        this.npcs = npcs;
    }

    public void startAI(){
        startMovement();
        startConversation();
    }

    private void startMovement(){
        movementTask = Bukkit.getScheduler().runTaskTimer(plugin,()->{
            for(Villager npc : new ArrayList<>(npcs)){
                if(!npc.isValid()) continue;

                Location loc = npc.getLocation();

                if(playerNearby(loc)) continue;

                double dx = (random.nextDouble()-0.5)*8;
                double dz = (random.nextDouble()-0.5)*8;

                Location target = loc.clone().add(dx,0,dz);

                Vector direction = target.toVector().subtract(loc.toVector()).normalize();

                npc.setVelocity(direction.multiply(0.25));
            }
        },20*5,20*5);
    }

    private void startConversation(){
        conversationTask = Bukkit.getScheduler().runTaskTimer(plugin,()->{
            if(npcs.size()<2) return;

            Villager npc1 = npcs.get(random.nextInt(npcs.size()));
            Villager npc2 = npcs.get(random.nextInt(npcs.size()));

            if(!npc1.isValid() || !npc2.isValid()) return;
            if(npc1.equals(npc2)) return;
            if(npc1.getLocation().distance(npc2.getLocation())>8) return;

            String name1 = npc1.getCustomName();
            String name2 = npc2.getCustomName();
            if(name1==null || name2==null) return;

            Bukkit.broadcastMessage("§7<"+name1+"> §fHast du das gesehen?");
            Bukkit.getScheduler().runTaskLater(plugin,()->{
                Bukkit.broadcastMessage("§7<"+name2+"> §fNein… irgendwas stimmt hier nicht.");
            },40);
        },20*60,20*120);
    }

    private boolean playerNearby(Location loc){
        for(Player p : loc.getWorld().getPlayers()){
            if(p.getLocation().distance(loc)<6) return true;
        }
        return false;
    }
}
