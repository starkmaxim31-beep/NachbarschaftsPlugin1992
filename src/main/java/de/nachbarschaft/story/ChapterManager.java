package de.nachbarschaft.story;

import de.nachbarschaft.Main;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChapterManager {

    private final Map<UUID, Integer> playerChapters = new HashMap<>();

    // Kapitel setzen
    public void setChapter(Player player, int chapter) {

        playerChapters.put(player.getUniqueId(), chapter);

        player.sendMessage("§eKapitel gesetzt auf §6" + chapter);

        // Belohnungen prüfen
        giveChapterRewards(player, chapter);

    }

    // Kapitel abrufen
    public int getChapter(Player player) {

        return playerChapters.getOrDefault(
                player.getUniqueId(),
                1
        );

    }

    // Nächstes Kapitel
    public void nextChapter(Player player) {

        int current = getChapter(player);
        int next = current + 1;

        setChapter(player, next);

    }

    // Kapitel zurücksetzen
    public void resetChapter(Player player) {

        playerChapters.put(
                player.getUniqueId(),
                1
        );

        player.sendMessage("§cKapitel wurde zurückgesetzt.");

    }

    // Kapitel-Belohnungen
    private void giveChapterRewards(Player player, int chapter) {

        switch (chapter) {

            case 3:

                Main.getInstance()
                        .getSoulWeaponManager()
                        .giveWeapon(
                                player,
                                "Seelenklinge"
                        );

                player.sendMessage("§dDu hast deine erste Seelenwaffe erhalten!");
                break;

            case 5:

                Main.getInstance()
                        .getSoulWeaponManager()
                        .giveWeapon(
                                player,
                                "Seelenstab"
                        );

                player.sendMessage("§5Eine neue Macht wurde freigeschaltet!");
                break;

            case 10:

                player.sendMessage("§cEin Boss ist erschienen!");
                break;

            case 20:

                player.sendMessage("§6Das Finale beginnt...");
                break;

        }

    }

}
