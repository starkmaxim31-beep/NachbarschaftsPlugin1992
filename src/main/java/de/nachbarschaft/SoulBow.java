package de.nachbarschaft.soul.weapon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SoulBow {

    public static ItemStack create() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        meta.setDisplayName("§bSeelenbogen des Himmels");
        meta.setLore(List.of(
                "§7Pfeilregen",
                "§7Superskill",
                "§73 Sekunden Flug"
        ));

        bow.setItemMeta(meta);
        return bow;
    }

    public static boolean isSoulBow(ItemStack item) {
        return item != null
                && item.getType() == Material.BOW
                && item.getItemMeta() != null
                && "§bSeelenbogen des Himmels".equals(item.getItemMeta().getDisplayName());
    }
}

