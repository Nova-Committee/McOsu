package top.pas.mcosu.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.pas.mcosu.McOsu;
import top.pas.mcosu.osu.OsuPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    public Map<UUID, Boolean> onlineMap;
    public Map<UUID, OsuPlayer> dataMap;

    public PlayerManager() {
        onlineMap = new HashMap<>();
        dataMap = new HashMap<>();
    }

    public void tick() {
        onlineMap.forEach((UUID player, Boolean online) -> onlineMap.replace(player, false));

        // 遍历所有在线玩家
        for (Player player : McOsu.INSTANCE.getServer().getOnlinePlayers()) {
            final UUID uuid = player.getUniqueId();
            if (!onlineMap.containsKey(uuid)) {
                // 添加不被包含在表中的玩家
                onlineMap.put(uuid, true);
                dataMap.put(uuid, new OsuPlayer(player));
            }
            // 更新每名玩家
            update(player, uuid);
        }
    }

    private void update(Player player, UUID uuid) {
        onlineMap.replace(uuid, true);
        try {
            dataMap.get(uuid).tick();
        } catch (NullPointerException e) {
            McOsu.LOGGER.warning(String.format("玩家 %s 的对应Osu实例不存在！丢弃玩家。", player.getName()));
            onlineMap.remove(uuid);
        }
    }

    public void clear() {
        for (OsuPlayer player : dataMap.values()) {
            player.handleStopGame();
        }
        onlineMap.clear();
        dataMap.clear();
    }

    public OsuPlayer getPlayer(@NotNull Player player) throws NullPointerException {
        final UUID uuid = player.getUniqueId();
        if (onlineMap.containsKey(uuid) && onlineMap.get(uuid).equals(true))
            return dataMap.get(uuid);
        throw new NullPointerException("Couldn't found any invalid osu-player with uuid %s".formatted(uuid));
    }
}
