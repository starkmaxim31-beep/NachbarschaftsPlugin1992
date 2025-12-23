package de.nachbarschaft;

import org.bukkit.entity.Player;

public class TitleManager {

    public static void kapitelTitle(Player p, String big, String small) {
        p.sendTitle("§6" + big, "§f" + small, 20, 80, 20);
    }
}
