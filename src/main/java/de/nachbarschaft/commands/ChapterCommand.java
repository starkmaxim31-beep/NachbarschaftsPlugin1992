package de.nachbarschaft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nachbarschaft.story.ChapterManager;

public class ChapterCommand implements CommandExecutor {

    private ChapterManager chapterManager;

    public ChapterCommand(ChapterManager chapterManager) {

        this.chapterManager = chapterManager;

    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(
                    "Nur Spieler können das benutzen."
            );

            return true;

        }

        Player player =
                (Player) sender;

        if (args.length != 1) {

            player.sendMessage(
                    "§cBenutzung: /chapter <nummer>"
            );

            return true;

        }

        try {

            int chapter =
                    Integer.parseInt(
                            args[0]
                    );

            chapterManager.setChapter(
                    player.getUniqueId(),
                    chapter
            );

            player.sendMessage(
                    "§aKapitel gesetzt auf "
                            + chapter
            );

        } catch (NumberFormatException e) {

            player.sendMessage(
                    "§cBitte eine Zahl eingeben."
            );

        }

        return true;

    }

}
