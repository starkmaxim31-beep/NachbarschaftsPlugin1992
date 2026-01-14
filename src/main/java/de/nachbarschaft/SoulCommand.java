package de.nachbarschaft.soul;

import de.nachbarschaft.soul.weapon.SoulBow;
import de.nachbarschaft.soul.weapon.SoulSword;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoulCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage("§c/soul sword | bow");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "sword" -> player.getInventory().addItem(SoulSword.create());
            case "bow" -> player.getInventory().addItem(SoulBow.create());
        }

        return true;
    }
}

