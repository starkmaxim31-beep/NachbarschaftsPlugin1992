package de.nachbarschaft.commands;

import de.nachbarschaft.Main;
import de.nachbarschaft.story.ChapterManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChapterCommand implements CommandExecutor {

    private ChapterManager chapterManager;

    public ChapterCommand() {
        this.chapterManager = Main.getInstance().getChapterManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Command benutzen.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Benutzung: /chapter get oder /chapter set <zahl>");
            return true;
        }

        if (args[0].equalsIgnoreCase("get")) {

            int chapter = chapterManager.getChapter(player.getUniqueId());
            player.sendMessage("Dein aktuelles Kapitel ist: " + chapter);
            return true;

        }

        if (args[0].equalsIgnoreCase("set")) {

            if (args.length < 2) {
                player.sendMessage("Bitte gib ein Kapitel an.");
                return true;
            }

            try {

                int newChapter = Integer.parseInt(args[1]);
                chapterManager.setChapter(player.getUniqueId(), newChapter);

                player.sendMessage("Kapitel wurde gesetzt auf: " + newChapter);

            } catch (NumberFormatException e) {

                player.sendMessage("Bitte eine gültige Zahl eingeben.");

            }

            return true;
        }

        return true;
    }
}
