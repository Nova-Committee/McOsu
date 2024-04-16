package top.pas.mcosu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.pas.mcosu.commands.CommandManager;

import java.util.logging.Logger;

public final class McOsu extends JavaPlugin {
    public static McOsu INSTANCE = null;
    public static Logger LOGGER = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        LOGGER = getLogger();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        INSTANCE = null;
        LOGGER = null;
        saveConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return CommandManager.onCommand(sender, command, label, args);
    }
}
