package top.pas.mcosu.osu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import top.pas.mcosu.McOsu;

import java.nio.file.Path;

@Getter
public class OsuPlayer {
    private final Player player;
    private boolean inGame = false;
    @Nullable
    private OsuMap currentGame;

    public OsuPlayer(Player player) {
        this.player = player;
    }

    public boolean handleLoadMap(Path map) {
        try {
            currentGame = OsuMap.createMap(map, player);
            return true;
        } catch (Exception e) {
            McOsu.LOGGER.info("Failed to load osu map at path: '%s'\n%s".formatted(map.toAbsolutePath(), e));
            return false;
        }
    }

    public boolean handleLoadMap(String map) {
        try {
            currentGame = new OsuMap(map, player);
            return true;
        } catch (Exception e) {
            McOsu.LOGGER.info("Failed to load osu map as string: '%s'".formatted(map));
            return false;
        }
    }

    public boolean handleStartGame() {
        if (currentGame == null) return false;
        inGame = true;
        return true;
    }

    public void handleStopGame() {
        if (inGame && currentGame != null) {
            currentGame.stop();
            currentGame = null;
            inGame = false;
        }
    }

    public void tick() {
        // TODO handle sound
        if (inGame && currentGame != null) {
            currentGame.tick();
        }
    }
}
