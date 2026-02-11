package de.nachbarschaft;

import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class RuleManager {

    private static final int MAX_NORMAL_RULES = 3;
    private static final int MAX_ABSOLUTE_RULES = 1;
    private static final Duration ABSOLUTE_DURATION = Duration.ofHours(24);
    private static final String RULE_SEPARATOR = "||";

    private final JavaPlugin plugin;

    public RuleManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void handleRuleCommand(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("list")) {
            listRules(sender);
            return;
        }
        String action = args[0].toLowerCase(Locale.GERMAN);
        if ("add".equals(action)) {
            addRule(sender, args);
            return;
        }
        if ("remove".equals(action)) {
            removeRule(sender, args);
            return;
        }
        sender.sendMessage("§e/regel list");
        sender.sendMessage("§e/regel add <normal|absolut> <text>");
        sender.sendMessage("§e/regel remove <normal|absolut> <index>");
    }

    private void addRule(CommandSender sender, String[] args) {
        if (!isRepeatingCommandBlock(sender)) {
            sender.sendMessage("§7Nur der lila Commandblock darf Regeln hinzufügen.");
            return;
        }
        if (args.length < 3) {
            sender.sendMessage("§e/regel add <normal|absolut> <text>");
            return;
        }
        String type = args[1].toLowerCase(Locale.GERMAN);
        String text = String.join(" ", List.of(args).subList(2, args.length)).trim();
        if (text.isBlank()) {
            sender.sendMessage("§7Der Regeltext darf nicht leer sein.");
            return;
        }
        pruneExpired();
        if ("normal".equals(type)) {
            List<String> normalRules = getNormalRules();
            if (normalRules.size() >= MAX_NORMAL_RULES) {
                sender.sendMessage("§7Es sind bereits " + MAX_NORMAL_RULES + " normale Regeln gesetzt.");
                return;
            }
            normalRules.add(text);
            saveNormalRules(normalRules);
            sender.sendMessage("§aNormale Regel hinzugefügt.");
            return;
        }
        if ("absolut".equals(type)) {
            List<RuleEntry> absoluteRules = getAbsoluteRules();
            if (absoluteRules.size() >= MAX_ABSOLUTE_RULES) {
                sender.sendMessage("§7Es ist bereits eine absolute Regel aktiv.");
                return;
            }
            long expiresAt = Instant.now().plus(ABSOLUTE_DURATION).toEpochMilli();
            absoluteRules.add(new RuleEntry(text, expiresAt));
            saveAbsoluteRules(absoluteRules);
            sender.sendMessage("§dAbsolute Regel gesetzt (24h).");
            return;
        }
        sender.sendMessage("§eTypen: normal, absolut");
    }

    private void removeRule(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§e/regel remove <normal|absolut> <index>");
            return;
        }
        String type = args[1].toLowerCase(Locale.GERMAN);
        int index;
        try {
            index = Integer.parseInt(args[2]) - 1;
        } catch (NumberFormatException ex) {
            sender.sendMessage("§7Index muss eine Zahl sein.");
            return;
        }
        pruneExpired();
        if ("normal".equals(type)) {
            List<String> normalRules = getNormalRules();
            if (index < 0 || index >= normalRules.size()) {
                sender.sendMessage("§7Ungültiger Index.");
                return;
            }
            normalRules.remove(index);
            saveNormalRules(normalRules);
            sender.sendMessage("§aNormale Regel entfernt.");
            return;
        }
        if ("absolut".equals(type)) {
            List<RuleEntry> absoluteRules = getAbsoluteRules();
            if (index < 0 || index >= absoluteRules.size()) {
                sender.sendMessage("§7Ungültiger Index.");
                return;
            }
            absoluteRules.remove(index);
            saveAbsoluteRules(absoluteRules);
            sender.sendMessage("§dAbsolute Regel entfernt.");
            return;
        }
        sender.sendMessage("§eTypen: normal, absolut");
    }

    private void listRules(CommandSender sender) {
        pruneExpired();
        List<String> normalRules = getNormalRules();
        List<RuleEntry> absoluteRules = getAbsoluteRules();
        sender.sendMessage("§eRegeln:");
        if (normalRules.isEmpty()) {
            sender.sendMessage("§7- Keine normalen Regeln");
        } else {
            for (int i = 0; i < normalRules.size(); i++) {
                sender.sendMessage("§7" + (i + 1) + ". §f" + normalRules.get(i));
            }
        }
        if (absoluteRules.isEmpty()) {
            sender.sendMessage("§7- Keine absoluten Regeln");
        } else {
            for (int i = 0; i < absoluteRules.size(); i++) {
                RuleEntry rule = absoluteRules.get(i);
                sender.sendMessage("§d" + (i + 1) + ". §f" + rule.text + " §7(" + rule.remainingHours() + "h)");
            }
        }
    }

    private boolean isRepeatingCommandBlock(CommandSender sender) {
        if (!(sender instanceof BlockCommandSender blockSender)) {
            return false;
        }
        Material type = blockSender.getBlock().getType();
        return type == Material.REPEATING_COMMAND_BLOCK;
    }

    private void pruneExpired() {
        List<RuleEntry> absoluteRules = getAbsoluteRules();
        long now = Instant.now().toEpochMilli();
        List<RuleEntry> active = new ArrayList<>();
        for (RuleEntry entry : absoluteRules) {
            if (entry.expiresAt > now) {
                active.add(entry);
            }
        }
        if (active.size() != absoluteRules.size()) {
            saveAbsoluteRules(active);
        }
    }

    private List<String> getNormalRules() {
        return new ArrayList<>(plugin.getConfig().getStringList("rules.normal"));
    }

    private void saveNormalRules(List<String> rules) {
        FileConfiguration cfg = plugin.getConfig();
        cfg.set("rules.normal", rules);
        plugin.saveConfig();
    }

    private List<RuleEntry> getAbsoluteRules() {
        List<String> raw = plugin.getConfig().getStringList("rules.absolute");
        List<RuleEntry> entries = new ArrayList<>();
        for (String line : raw) {
            int separatorIndex = line.indexOf(RULE_SEPARATOR);
            if (separatorIndex <= 0) {
                continue;
            }
            String expiresText = line.substring(0, separatorIndex);
            String ruleText = line.substring(separatorIndex + RULE_SEPARATOR.length());
            try {
                long expiresAt = Long.parseLong(expiresText.trim());
                entries.add(new RuleEntry(ruleText, expiresAt));
            } catch (NumberFormatException ignored) {
                // skip invalid entries
            }
        }
        entries.sort(Comparator.comparingLong(entry -> entry.expiresAt));
        return entries;
    }

    private void saveAbsoluteRules(List<RuleEntry> rules) {
        FileConfiguration cfg = plugin.getConfig();
        List<String> raw = new ArrayList<>();
        for (RuleEntry entry : rules) {
            raw.add(entry.expiresAt + RULE_SEPARATOR + entry.text);
        }
        cfg.set("rules.absolute", raw);
        plugin.saveConfig();
    }

    private static class RuleEntry {
        private final String text;
        private final long expiresAt;

        private RuleEntry(String text, long expiresAt) {
            this.text = text;
            this.expiresAt = expiresAt;
        }

        private long remainingHours() {
            long remainingMillis = Math.max(0, expiresAt - Instant.now().toEpochMilli());
            return Math.max(1, Duration.ofMillis(remainingMillis).toHours());
        }
    }
}
