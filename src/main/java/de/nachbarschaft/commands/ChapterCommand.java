package de.nachbarschaft.commands;

import de.nachbarschaft.Main;
import de.nachbarschaft.story.ChapterManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChapterCommand implements CommandExecutor {

    private final ChapterManager chapterManager;

    public ChapterCommand() {
        this.chapterManager = Main.getInstance().getChapterManager();
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {

            int chapter = chapterManager.getChapter(player);

            player.sendMessage("§eDein Kapitel: §6" + chapter);
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {

            if (args.length < 2) {
                player.sendMessage("§cBenutzung: /chapter set <zahl>");
                return true;
            }

            try {

                int chapter = Integer.parseInt(args[1]);

                chapterManager.setChapter(player, chapter);

            } catch (NumberFormatException e) {

                player.sendMessage("§cBitte eine gültige Zahl eingeben.");

            }

            return true;
        }

        if (args[0].equalsIgnoreCase("next")) {

            chapterManager.nextChapter(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {

            chapterManager.resetChapter(player);
            return true;
        }

        player.sendMessage("§cUnbekannter Befehl.");
        return true;
    }
}
