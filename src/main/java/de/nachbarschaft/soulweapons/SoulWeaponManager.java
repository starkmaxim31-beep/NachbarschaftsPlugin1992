package de.nachbarschaft.soulweapons;

import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class SoulWeapon {

    private String name;

    public SoulWeapon(String name) {

        this.name = name;

    }

    public String getName() {

        return name;

    }

    // Wird beim Erhalten der Waffe ausgeführt
    public abstract void activate(Player player);

    // Fähigkeit der Waffe
    public abstract void ability(Player player);

    // Cooldown / Nutzung prüfen
    public abstract boolean canUse(UUID uuid);

}
