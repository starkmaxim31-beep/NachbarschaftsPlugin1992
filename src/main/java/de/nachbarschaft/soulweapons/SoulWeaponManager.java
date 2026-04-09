package de.nachbarschaft.soulweapons;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class SoulWeaponManager {

    private final Map<String, SoulWeapon> weapons = new HashMap<>();

    // Konstruktor
    public SoulWeaponManager() {

        // Hier registrieren wir die Waffen
        registerWeapon(new SoulSword());

    }

    // Waffe registrieren
    public void registerWeapon(SoulWeapon weapon) {
        weapons.put(weapon.getName(), weapon);
    }

    // Waffe geben
    public void giveWeapon(Player player, String weaponName) {

        SoulWeapon weapon = weapons.get(weaponName);

        if (weapon == null) {
            player.sendMessage("§cDiese Seelenwaffe existiert nicht!");
            return;
        }

        player.getInventory().addItem(weapon.createItem());

        player.sendMessage("§aDu hast die Seelenwaffe erhalten: §6" + weaponName);
    }

    // Prüfen ob Spieler eine Waffe hat
    public boolean hasWeapon(Player player) {

        for (SoulWeapon weapon : weapons.values()) {

            if (player.getInventory().contains(weapon.createItem())) {
                return true;
            }

        }

        return false;
    }

    // Fähigkeit benutzen
    public void useAbility(Player player) {

        for (SoulWeapon weapon : weapons.values()) {

            if (player.getInventory().contains(weapon.createItem())) {

                weapon.activateAbility(player);
                return;

            }

        }

        player.sendMessage("§cDu besitzt keine Seelenwaffe!");
    }

}
