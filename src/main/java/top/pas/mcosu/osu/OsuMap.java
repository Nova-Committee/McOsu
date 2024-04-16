package top.pas.mcosu.osu;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.pas.mcosu.McOsu;
import top.pas.mcosu.osu.utils.AudioObject;
import top.pas.mcosu.osu.utils.HitObject;
import top.pas.mcosu.utils.PlayerDirection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OsuMap {
    public static final List<String> PART_NAME = List.of(
            "[General]", "[Editor]", "[MetaData]", "[Difficulty]", "[Events]", "[TimingPoints]", "[Colours]", "[HitObjects]");
    public final String rawMap;
    /**
     * Key: 原始文件中的部分，如[General]
     * Value: 该部分下的所有项
     */
    public final Map<String, List<String>> rawData;
    public final String title;
    public final AudioObject audio;
    public final List<HitObject> hitObjects = new LinkedList<>();

    public OsuMap(String stringMap, @NotNull Player player) {
        this.rawMap = stringMap;

        // 常量
        final double HORIZON_SIZE = McOsu.INSTANCE.getConfig().getDouble("osu.screen.horizon-size");
        final double VERTICAL_SIZE = McOsu.INSTANCE.getConfig().getDouble("osu.screen.vertical-size");;
        final double TICK_SPEED = McOsu.INSTANCE.getConfig().getDouble("osu.hit-object.speed");
        final double HIT_DISTANCE = McOsu.INSTANCE.getConfig().getDouble("osu.hit-object.hit-distance");
        final Location playerPos = player.getLocation();

        // 解析原始铺面信息
        this.rawData = getData(rawMap);

        final Map<String, String> general = getMapData(rawData.get(PART_NAME.get(0)), ": ");
        this.audio = new AudioObject(Path.of(general.get("AudioFileName")), Integer.getInteger(general.get("AudioLeadIn")));

        final Map<String, String> metaData = getMapData(rawData.get(PART_NAME.get(2)), ": ");
        this.title = metaData.containsKey("TitleUnicode") ? metaData.get("TitleUnicode") : metaData.get("Title");

        final List<List<String>> hitObjects = getDotData(rawData.get(PART_NAME.get(7)), ",");
        for (List<String> rawHitObject : hitObjects) {
            // 重映射
            final double xPos = Double.parseDouble(rawHitObject.get(0)) / 640
                    * HORIZON_SIZE;
            final double yPos = Double.parseDouble(rawHitObject.get(1)) / 480
                    * VERTICAL_SIZE;
            final double distance = Math.round(Double.parseDouble(rawHitObject.get(2)) / 50)
                    * TICK_SPEED
                    + HIT_DISTANCE;
            // 检查玩家朝向
            final PlayerDirection direction = PlayerDirection.fromYaw(playerPos.getYaw());

            final Location targetLocation;
            switch (direction) {
                // PositiveX
                default ->
                        targetLocation = new Location(player.getWorld(), playerPos.getX() + HIT_DISTANCE, playerPos.getY() + yPos, playerPos.getZ() + xPos);
                case NegativeX ->
                        targetLocation = new Location(player.getWorld(), playerPos.getX() - HIT_DISTANCE, playerPos.getY() + yPos, playerPos.getZ() + xPos);
                case PositiveZ ->
                        targetLocation = new Location(player.getWorld(), playerPos.getX() + xPos, playerPos.getY() + yPos, playerPos.getZ() + HIT_DISTANCE);
                case NegativeZ ->
                        targetLocation = new Location(player.getWorld(), playerPos.getX() + xPos, playerPos.getY() + yPos, playerPos.getZ() - HIT_DISTANCE);
            }
            this.hitObjects.add(
                    new HitObject(targetLocation, direction.getReverse(), distance, TICK_SPEED)
            );
        }
    }

    private static @NotNull Map<String, List<String>> getData(@NotNull String rawMap) {
        Map<String, List<String>> data = new HashMap<>();

        boolean foundPart = false;
        String currentPart = null;
        List<String> currentPartData = null;
        for (String line : rawMap.split("\n")) {
            if (!foundPart && PART_NAME.contains(line)) {  // 找到有效部分，跳过该行转到下一行有效数据
                foundPart = true;
                currentPart = line;
                currentPartData = new LinkedList<>();
                continue;
            }
            if (foundPart && line.isEmpty()) {  // 一段有效部分结束
                foundPart = false;
                data.put(currentPart, currentPartData);
            }
            if (foundPart) {  // 正处于有效部分
                currentPartData.add(line);
            }
        }

        return data;
    }

    private static @NotNull Map<String, String> getMapData(@NotNull List<String> rawData, String spilt) {
        Map<String, String> map = new HashMap<>();
        for (String line : rawData) {
            final String[] kv = line.split(spilt);
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    private static @NotNull List<List<String>> getDotData(@NotNull List<String> rawData, String spilt) {
        List<List<String>> list = new ArrayList<>(rawData.size());
        for (String line : rawData) {
            list.add(List.of(line.split(spilt)));
        }
        return list;
    }

    @Contract("_, _ -> new")
    @NotNull
    public static OsuMap createMap(Path osuFile, Player player) throws IOException {
        if (!Files.exists(osuFile))
            throw new FileNotFoundException("couldn't load osu-map from path '%s'".formatted(osuFile));

        return new OsuMap(Files.readString(osuFile), player);
    }

    public void tick() {
        for (HitObject object : hitObjects) {
            object.tick();
        }
    }
}
