package de.nachbarschaft.chapter;

import java.util.HashMap;
import java.util.UUID;

public class ChapterManager {

    private static final HashMap<UUID, Integer> chapters = new HashMap<>();

    public static void setChapter(UUID uuid, int chapter) {
        chapters.put(uuid, chapter);
    }

    public static int getChapter(UUID uuid) {
        return chapters.getOrDefault(uuid, 1);
    }
}
