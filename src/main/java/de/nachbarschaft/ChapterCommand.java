package de.nachbarschaft.chapter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChapterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage("§c/chapter <1-20>");
            return true;
        }

        int chapter = Integer.parseInt(args[0]);
        ChapterManager.setChapter(player.getUniqueId(), chapter);
        player.sendMessage("§aKapitel gesetzt auf §e" + chapter);

        return true;
    }
}

