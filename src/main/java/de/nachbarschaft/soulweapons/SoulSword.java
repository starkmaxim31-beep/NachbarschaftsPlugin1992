package de.nachbarschaft.soulweapons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SoulSword implements SoulWeapon {

    @Override
    public String getName() {
        return "Seelenklinge";
    }

    @Override
    public ItemStack createItem() {

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§5Seelenklinge");
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public void activateAbility(Player player) {

        player.sendMessage("§dDie Macht der Seelenklinge erwacht!");

        player.setVelocity(
                player.getLocation()
                        .getDirection()
                        .multiply(1.5)
        );

    }

}
