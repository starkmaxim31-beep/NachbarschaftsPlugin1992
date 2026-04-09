package de.nachbarschaft.soulweapons;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class SoulWeaponManager {

    private Map<String, SoulWeapon> weapons =
            new HashMap<>();

    private Map<UUID, SoulWeapon> playerWeapons =
            new HashMap<>();

    public SoulWeaponManager() {

        // Hier registrieren wir Waffen
        // registerWeapon(new SoulSword());

    }

    public void registerWeapon(SoulWeapon weapon) {

        weapons.put(
                weapon.getName(),
                weapon
        );

    }

    public void giveWeapon(
            Player player,
            String weaponName
    ) {

        SoulWeapon weapon =
                weapons.get(weaponName);

        if (weapon == null) {

            player.sendMessage(
                    "§cDiese Seelenwaffe existiert nicht."
            );

            return;

        }

        playerWeapons.put(
                player.getUniqueId(),
                weapon
        );

        weapon.activate(player);

    }

    public void useAbility(Player player) {

        SoulWeapon weapon =
                playerWeapons.get(
                        player.getUniqueId()
                );

        if (weapon == null) {

            player.sendMessage(
                    "§cDu besitzt keine Seelenwaffe."
            );

            return;

        }

        weapon.ability(player);

    }

}

public boolean hasWeapon(Player player) {

    return playerWeapons.containsKey(
            player.getUniqueId()
    );

}
