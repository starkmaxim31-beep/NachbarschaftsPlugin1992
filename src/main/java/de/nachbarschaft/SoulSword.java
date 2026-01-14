package de.nachbarschaft.soul.weapon;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SoulSword {

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§5Seelenschwert von Yami");
        meta.setLore(List.of(
                "§7Seelengebunden",
                "§dDash §7(Rechtsklick)",
                "§dMulti-Hit",
                "§dLuftangriff"
        ));

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isSoulSword(ItemStack item) {
        return item != null
                && item.getType() == Material.NETHERITE_SWORD
                && item.getItemMeta() != null
                && "§5Seelenschwert von Yami".equals(item.getItemMeta().getDisplayName());
    }
}

