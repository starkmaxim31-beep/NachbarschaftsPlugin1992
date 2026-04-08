package de.nachbarschaft.trigger;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.nachbarschaft.story.ChapterManager;

public class PlayerMoveListener
        implements Listener {

    private List<ZoneTrigger> zones =
            new ArrayList<>();

    public PlayerMoveListener(
            ChapterManager chapterManager
    ) {

        zones.add(
                new ZoneTrigger(
                        chapterManager,

                        new Location(
                                null,
                                100,
                                64,
                                100
                        ),

                        new Location(
                                null,
                                110,
                                70,
                                110
                        ),

                        2
                )
        );

    }

    @EventHandler
    public void onMove(
            PlayerMoveEvent event
    ) {

        for (ZoneTrigger zone :
                zones) {

            zone.check(
                    event.getPlayer()
            );

        }

    }

}
