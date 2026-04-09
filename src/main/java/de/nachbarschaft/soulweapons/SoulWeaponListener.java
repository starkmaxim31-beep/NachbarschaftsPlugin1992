package de.nachbarschaft.soulweapons;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;

import de.nachbarschaft.Main;

public class SoulWeaponListener implements Listener {

    @EventHandler
    public void onPlayerInteract(
            PlayerInteractEvent event
    ) {

        Action action =
                event.getAction();

        // Nur Rechtsklick
        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player =
                event.getPlayer();

        SoulWeaponManager manager =
                Main.getInstance()
                        .getSoulWeaponManager();

        // Prüfen ob Spieler eine Waffe hat
        if (!manager.hasWeapon(player)) {
            return;
        }

        // Fähigkeit benutzen
        manager.useAbility(player);

    }

}
