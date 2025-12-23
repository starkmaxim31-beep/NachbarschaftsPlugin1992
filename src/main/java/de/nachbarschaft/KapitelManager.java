package de.nachbarschaft;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class KapitelManager {

    private final Main plugin;

    public KapitelManager(Main plugin) {
        this.plugin = plugin;
    }

    public void startStory(Player p) {
        plugin.getConfig().set("players." + p.getUniqueId() + ".kapitel", 1);
        plugin.saveConfig();

        TitleManager.kapitelTitle(p, "Kapitel 1", "Ankunft auf der Insel");
        SoundManager.startSound(p);

        p.sendMessage("§7Du spürst... hier stimmt etwas nicht.");
    }

    public void nextKapitel(Player p) {
        FileConfiguration cfg = plugin.getConfig();
        int kapitel = cfg.getInt("players." + p.getUniqueId() + ".kapitel", 1);

        kapitel++;

        if (kapitel > 5) {
            p.sendMessage("§aPhase 2 ist abgeschlossen!");
            return;
        }

        cfg.set("players." + p.getUniqueId() + ".kapitel", kapitel);
        plugin.saveConfig();

        switch (kapitel) {

            case 2:
                TitleManager.kapitelTitle(p, "Kapitel 2", "Oberstadt & Unterstadt");
                SoundManager.storySound(p);
                p.sendMessage("§7Zwei Wege. Zwei Welten. Beide gefährlich…");
                break;

            case 3:
                TitleManager.kapitelTitle(p, "Kapitel 3", "Ein Admin erscheint");
                SoundManager.adminSound(p);
                p.sendMessage("§8Dunkelheit breitet sich aus… jemand beobachtet dich.");
                break;

            case 4:
                TitleManager.kapitelTitle(p, "Kapitel 4", "Misstrauen");
                SoundManager.darkSound(p);
                p.sendMessage("§cEtwas stimmt nicht… Grün? Lila? Wer sagt die Wahrheit?");
                break;

            case 5:
                TitleManager.kapitelTitle(p, "Kapitel 5", "Der Pfad zum Sanctum");
                SoundManager.epicSound(p);
                p.sendMessage("§6Es gibt mehr als Admins… eine größere Macht erwacht.");
                break;
        }
    }

    public void info(Player p) {
        int kapitel = plugin.getConfig().getInt("players." + p.getUniqueId() + ".kapitel", 1);
        p.sendMessage("§eDu bist aktuell in Kapitel: §a" + kapitel);
    }
}
