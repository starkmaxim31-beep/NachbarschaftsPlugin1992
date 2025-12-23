package de.nachbarschaft;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {

    public static void startSound(Player p) {
        p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1f, 1f);
    }

    public static void storySound(Player p) {
        p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1f, 1f);
    }

    public static void adminSound(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f);
    }

    public static void darkSound(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1f);
    }

    public static void epicSound(Player p) {
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    }
}
