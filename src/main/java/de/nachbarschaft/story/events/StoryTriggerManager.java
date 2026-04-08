package de.nachbarschaft.story.events;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StoryTriggerManager {

    private Set<UUID> triggeredPlayers = new HashSet<>();

    public void checkChapterTrigger(Player player, int chapter) {

        UUID uuid = player.getUniqueId();

        if (triggeredPlayers.contains(uuid)) {
            return;
        }

        switch (chapter) {

            case 1:

                player.sendMessage("§6Willkommen in der Nachbarschaft.");
                player.playSound(player.getLocation(),
                        Sound.ENTITY_PLAYER_LEVELUP,
                        1f,
                        1f);

                triggeredPlayers.add(uuid);
                break;

            case 5:

                Bukkit.broadcastMessage(
                        "§5Ein dunkles Ritual beginnt...");

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(
                            p.getLocation(),
                            Sound.ENTITY_WITHER_SPAWN,
                            1f,
                            1f);
                }

                triggeredPlayers.add(uuid);
                break;

            case 10:

                Bukkit.broadcastMessage(
                        "§cEine mächtige Präsenz erscheint.");

                triggeredPlayers.add(uuid);
                break;

        }

    }

}
