package top.pas.mcosu.osu.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.pas.mcosu.McOsu;
import top.pas.mcosu.utils.PlayerDirection;

@Getter
public class HitObject {
    private final Location targetLocation;
    private final PlayerDirection motionDirection;
    private double currentDistance;
    private final double tickDistance;

    @Nullable
    private Entity entity = null;
    protected double VIEW_DISTANCE = 32;

    public HitObject(Location targetLocation, PlayerDirection motionDirection, double currentDistance, double tickDistance) {
        this.targetLocation = targetLocation;
        this.motionDirection = motionDirection;
        this.currentDistance = currentDistance;
        this.tickDistance = tickDistance;
    }
    public void tick() {
        VIEW_DISTANCE = McOsu.INSTANCE.getConfig().getDouble("osu.hit-object.view-distance");

        if (entity == null && currentDistance <= VIEW_DISTANCE) {
            createEntity();
        }
        if (entity != null) {
            final World world = targetLocation.getWorld();
            if (world == null) return;

            entity.teleport(getLocation(world));
        }
        currentDistance -= tickDistance;
    }

    public void createEntity() {
        final World world = targetLocation.getWorld();
        if (world == null) return;

        McOsu.LOGGER.info("Create entity.");
        this.entity = world.spawnEntity(getLocation(world), EntityType.ARMOR_STAND);
        this.entity.setGravity(false);
    }

    public Location getLocation(@NotNull World world) {
        final Location location;
        switch (motionDirection) {
            // PositiveX
            default -> location = new Location(world, targetLocation.getX() - currentDistance, targetLocation.getY(), targetLocation.getZ());
            case PositiveZ -> location = new Location(world, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ() - currentDistance);
            case NegativeX -> location = new Location(world, targetLocation.getX() + currentDistance, targetLocation.getY(), targetLocation.getZ());
            case NegativeZ -> location = new Location(world, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ() + currentDistance);
        }
        return location;
    }

    public void stop() {
        if (entity == null) return;

        entity.remove();
        entity = null;
    }
}
