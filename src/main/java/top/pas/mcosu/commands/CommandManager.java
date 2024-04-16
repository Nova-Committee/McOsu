package top.pas.mcosu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
    public static boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player could play osu!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("mcosu")) {
            if (args.length == 0) {
                // TODO help message
            } else {
                switch (args[0]) {
                    case "help":  // TODO help message
                    case "play":  // TODO load map
                    case "start":  // TODO start game
                    case "stop":  // TODO stop game
                }
            }
            return true;
        }
        return false;
    }
}
