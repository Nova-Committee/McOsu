package top.pas.mcosu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.pas.mcosu.commands.CommandManager;
import top.pas.mcosu.utils.PlayerManager;
import top.pas.mcosu.utils.TickManager;

import java.util.logging.Logger;

public final class McOsu extends JavaPlugin {
    public static McOsu INSTANCE = null;
    public static Logger LOGGER = null;

    public TickManager tickManager;
    public PlayerManager playerManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        LOGGER = getLogger();
        saveDefaultConfig();
        tickManager = new TickManager();
        playerManager = new PlayerManager();

        tickManager.runTaskTimer(this, 0, 1);
        tickManager.register(playerManager::tick);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        tickManager.cancel();
        playerManager.clear();
        INSTANCE = null;
        LOGGER = null;
        saveConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return CommandManager.onCommand(sender, command, args);
    }
}
