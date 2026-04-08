package de.nachbarschaft.trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.nachbarschaft.story.ChapterManager;

public class ZoneTrigger {

    private ChapterManager chapterManager;

    private Map<UUID, Boolean> triggered =
            new HashMap<>();

    private Location pos1;
    private Location pos2;

    private int chapter;

    public ZoneTrigger(
            ChapterManager chapterManager,
            Location pos1,
            Location pos2,
            int chapter
    ) {

        this.chapterManager =
                chapterManager;

        this.pos1 = pos1;
        this.pos2 = pos2;

        this.chapter = chapter;

    }

    public void check(Player player) {

        if (isInside(player.getLocation())) {

            UUID uuid =
                    player.getUniqueId();

            if (triggered
                    .getOrDefault(uuid,
                            false)) {
                return;
            }

            chapterManager.setChapter(
                    uuid,
                    chapter
            );

            player.sendMessage(
                    "§aKapitel gestartet!"
            );

            triggered.put(uuid, true);

        }

    }

    private boolean isInside(
            Location loc
    ) {

        double minX =
                Math.min(
                        pos1.getX(),
                        pos2.getX()
                );

        double maxX =
                Math.max(
                        pos1.getX(),
                        pos2.getX()
                );

        double minY =
                Math.min(
                        pos1.getY(),
                        pos2.getY()
                );

        double maxY =
                Math.max(
                        pos1.getY(),
                        pos2.getY()
                );

        double minZ =
                Math.min(
                        pos1.getZ(),
                        pos2.getZ()
                );

        double maxZ =
                Math.max(
                        pos1.getZ(),
                        pos2.getZ()
                );

        return loc.getX() >= minX
                && loc.getX() <= maxX
                && loc.getY() >= minY
                && loc.getY() <= maxY
                && loc.getZ() >= minZ
                && loc.getZ() <= maxZ;

    }

}
