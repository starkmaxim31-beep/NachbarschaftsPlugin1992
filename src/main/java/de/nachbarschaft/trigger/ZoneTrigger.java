package de.nachbarschaft.trigger;

import de.nachbarschaft.Main;
import de.nachbarschaft.story.ChapterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class ZoneTrigger implements Listener {

    private final ChapterManager chapterManager;

    public ZoneTrigger() {
        this.chapterManager = Main.getInstance().getChapterManager();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        // Beispiel-Zone
        if (x > 100 && x < 110
                && y > 60 && y < 80
                && z > 100 && z < 110) {

            int chapter = chapterManager.getChapter(player);

            if (chapter == 2) {

                player.sendMessage("§aDu hast die Zone betreten!");

                chapterManager.nextChapter(player);

            }
        }
    }
}
