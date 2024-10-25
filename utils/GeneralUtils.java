package com.glatinis.basecraft.utils;

import com.glatinis.basecraft.BaseCraft;
import org.bukkit.scheduler.BukkitRunnable;

public class GeneralUtils {

    public static void runAfterDelay(Runnable task, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLater(BaseCraft.getPlugin(), delay);
    }
}
