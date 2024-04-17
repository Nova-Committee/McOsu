package top.pas.mcosu.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand {
    public static final String HELP_MESSAGE = """
                §r§b§lMcOsu 帮助信息§r
                §r/mcosu help §f- §7显示此帮助信息§r
                §r/mcosu play <path> §f- §7加载一个Osu铺面§r
                §r/mcosu start §f- §7开始游戏§r
                §r/mcosu stop §f- §7停止游戏§r
                """;

    public static void execute(@NotNull CommandSender sender) {
        sender.sendMessage(HELP_MESSAGE);
    }
}
