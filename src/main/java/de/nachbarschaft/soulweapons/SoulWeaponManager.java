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

    public abstract void activate(Player player);

    public abstract void ability(Player player);

    public abstract boolean canUse(UUID uuid);

}
