package top.pas.mcosu.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.pas.mcosu.McOsu;

public class StartCommand {
    public static void execute(@NotNull CommandSender sender) {
        if (sender instanceof Player player)
            McOsu.INSTANCE.playerManager.getPlayer(player).handleStartGame();
    }
}
