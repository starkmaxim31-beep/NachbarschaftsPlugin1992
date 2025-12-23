package de.nachbarschaft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KapitelCommand implements CommandExecutor {

    private final KapitelManager manager;

    public KapitelCommand(KapitelManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("§e/kapitel start");
            p.sendMessage("§e/kapitel weiter");
            p.sendMessage("§e/kapitel info");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                manager.startStory(p);
                break;

            case "weiter":
                manager.nextKapitel(p);
                break;

            case "info":
                manager.info(p);
                break;

            default:
                p.sendMessage("§cUnbekannter Befehl!");
                break;
        }

        return true;
    }
}

