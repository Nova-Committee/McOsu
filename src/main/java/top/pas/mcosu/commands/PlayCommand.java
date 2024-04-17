package top.pas.mcosu.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.pas.mcosu.McOsu;

import java.nio.file.Path;

public class PlayCommand {

    public static void execute(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /mcosu play <map_path>");
            return;
        }
        if (sender instanceof Player player)
            McOsu.INSTANCE.playerManager.getPlayer(player).handleLoadMap(Path.of(args[1]));
    }
}
