package de.nachbarschaft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ChapterManager chapterManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        chapterManager = new ChapterManager(this);
        getLogger().info("Nachbarschaft Kapitel-System aktiviert.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nachbarschaft Plugin gestoppt.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("kapitel")) {

            if(!(sender instanceof Player)) {
                sender.sendMessage("Nur Spieler können Kapitel benutzen.");
                return true;
            }

            Player p = (Player) sender;

            if(args.length == 0) {
                int kapitel = chapterManager.getChapter(p);
                p.sendMessage("§aDein aktuelles Kapitel: §e" + kapitel);
                return true;
            }

            if(args[0].equalsIgnoreCase("start")) {
                chapterManager.setChapter(p, 1);
                p.sendMessage("§6Kapitel §e1 §6gestartet!");
                return true;
            }

            if(args[0].equalsIgnoreCase("weiter")) {
                int chap = chapterManager.getChapter(p);
                chap++;
                chapterManager.setChapter(p, chap);
                p.sendMessage("§aDu bist jetzt in Kapitel §e" + chap);
                return true;
            }

            p.sendMessage("§cBenutze: /kapitel | /kapitel start | /kapitel weiter");
            return true;
        }

        return false;
    }
}

