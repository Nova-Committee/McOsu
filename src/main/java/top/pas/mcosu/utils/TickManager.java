package top.pas.mcosu.utils;

import org.bukkit.scheduler.BukkitRunnable;
import top.pas.mcosu.McOsu;

import java.util.LinkedList;
import java.util.List;

public class TickManager extends BukkitRunnable {
    private final List<Runnable> tickTasks;
    public TickManager() {
        tickTasks = new LinkedList<>();
    }

    public void register(Runnable task) {
        tickTasks.add(task);
    }

    @Override
    public void run() {
        for (Runnable task : tickTasks) {
            try {
                task.run();
            } catch (Exception e) {
                McOsu.LOGGER.warning("An error occurred while executing a tick-task. task: %s  error: %s".formatted(task, e));
            }
        }
    }
}
