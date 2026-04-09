package de.nachbarschaft.soulweapons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SoulWeapon {

    // Name der Waffe
    String getName();

    // Item erstellen
    ItemStack createItem();

    // Fähigkeit aktivieren
    void activateAbility(Player player);

}
