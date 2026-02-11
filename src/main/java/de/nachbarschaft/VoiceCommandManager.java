package de.nachbarschaft;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class VoiceCommandManager {

    private final JavaPlugin plugin;
    private final Map<String, VoiceCommand> commandMap = new HashMap<>();
    private final Map<UUID, Map<String, BukkitTask>> repeatTasks = new HashMap<>();

    public VoiceCommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public boolean triggerPhrase(Player player, String phrase) {
        if (phrase == null || phrase.isBlank()) {
            return false;
        }
        VoiceCommand command = commandMap.get(normalize(phrase));
        if (command == null) {
            return false;
        }
        if (command.repeat) {
            toggleRepeat(player, command);
            return true;
        }
        dispatchCommands(player, command);
        return true;
    }

    public void stopAllRepeats() {
        for (Map<String, BukkitTask> tasks : repeatTasks.values()) {
            for (BukkitTask task : tasks.values()) {
                task.cancel();
            }
        }
        repeatTasks.clear();
    }

    private void toggleRepeat(Player player, VoiceCommand command) {
        Map<String, BukkitTask> playerTasks = repeatTasks.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        BukkitTask existing = playerTasks.remove(command.key);
        if (existing != null) {
            existing.cancel();
            player.sendMessage("§dLila-Repeat beendet: §f" + command.key);
            return;
        }
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> dispatchCommands(player, command), 0L,
                20L * command.intervalSeconds);
        playerTasks.put(command.key, task);
        player.sendMessage("§dLila-Repeat gestartet: §f" + command.key);
    }

    private void dispatchCommands(Player player, VoiceCommand command) {
        CommandSender sender = command.asConsole ? Bukkit.getConsoleSender() : player;
        for (String cmd : command.commands) {
            if (cmd == null || cmd.isBlank()) {
                continue;
            }
            String prepared = cmd.replace("{player}", player.getName());
            Bukkit.dispatchCommand(sender, prepared);
        }
    }

    private void loadConfig() {
        FileConfiguration cfg = plugin.getConfig();
        if (!cfg.contains("voice-commands")) {
            addDefaultCommands(cfg);
        }
        ConfigurationSection section = cfg.getConfigurationSection("voice-commands");
        if (section == null) {
            plugin.saveConfig();
            return;
        }
        commandMap.clear();
        for (String key : section.getKeys(false)) {
            ConfigurationSection entry = section.getConfigurationSection(key);
            if (entry == null) {
                continue;
            }
            List<String> commands = entry.getStringList("commands");
            boolean repeat = entry.getBoolean("repeat", false);
            int interval = Math.max(1, entry.getInt("interval-seconds", 5));
            boolean asConsole = entry.getBoolean("as-console", false);
            commandMap.put(normalize(key), new VoiceCommand(key, commands, repeat, interval, asConsole));
        }
        plugin.saveConfig();
    }

    private void addDefaultCommands(FileConfiguration cfg) {
        cfg.set("voice-commands.sanctum.commands", List.of("sanctum"));
        cfg.set("voice-commands.kapitel.commands", List.of("kapitel"));
        cfg.set("voice-commands.seelenstart.commands", List.of("seelenstart"));
        cfg.set("voice-commands.lila.commands", List.of("sanctumwarn"));
        cfg.set("voice-commands.lila.repeat", true);
        cfg.set("voice-commands.lila.interval-seconds", 5);
        cfg.set("voice-commands.lila.as-console", false);
        cfg.options().copyDefaults(true);
    }

    private String normalize(String input) {
        return input.trim().toLowerCase(Locale.GERMAN);
    }

    private static class VoiceCommand {
        private final String key;
        private final List<String> commands;
        private final boolean repeat;
        private final int intervalSeconds;
        private final boolean asConsole;

        private VoiceCommand(String key, List<String> commands, boolean repeat, int intervalSeconds, boolean asConsole) {
            this.key = key;
            this.commands = commands;
            this.repeat = repeat;
            this.intervalSeconds = intervalSeconds;
            this.asConsole = asConsole;
        }
    }
}
