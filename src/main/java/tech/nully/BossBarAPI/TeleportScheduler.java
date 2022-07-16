package tech.nully.BossBarAPI;

import org.bukkit.scheduler.BukkitRunnable;

public class TeleportScheduler extends BukkitRunnable {
    private BossBar b;

    public TeleportScheduler(BossBar b) {
        this.b = b;
    }

    @Override
    public void run() {
        b.display();
    }
}
