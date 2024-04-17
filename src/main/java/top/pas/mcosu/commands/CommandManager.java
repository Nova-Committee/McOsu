package top.pas.mcosu.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
    public static boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player could play osu!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("mcosu")) {
            if (args.length == 0) {
                HelpCommand.execute(sender);
            } else {
                switch (args[0]) {
                    // help
                    default -> HelpCommand.execute(sender);
                    case "play" -> PlayCommand.execute(sender, args);
                    case "start" -> StartCommand.execute(sender);
                    case "stop" -> StopCommand.execute(sender);
                }
            }
            return true;
        }
        return false;
    }
}
