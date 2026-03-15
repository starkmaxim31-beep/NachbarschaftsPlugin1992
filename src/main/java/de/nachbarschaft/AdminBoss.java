package de.nachbarschaft.boss;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminBoss {

    private final JavaPlugin plugin;
    private Villager boss;

    public AdminBoss(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void spawnBoss(Location loc){
        boss = loc.getWorld().spawn(loc, Villager.class);
        boss.setCustomName("§cAdmin Boss");
        boss.setCustomNameVisible(true);
        boss.setAI(true);
        boss.setInvulnerable(false);
        Bukkit.broadcastMessage("§4Der Admin Boss ist erschienen!");
        startFight();
    }

    private void startFight(){
        Bukkit.getScheduler().runTaskTimer(plugin,()->{
            if(boss==null || boss.isDead()) return;
            Player target=null;
            for(Player p: boss.getWorld().getPlayers()){
                target=p; break;
            }
            if(target==null) return;
            boss.teleport(target.getLocation());
            target.damage(4);
        },40,60);
    }
}
