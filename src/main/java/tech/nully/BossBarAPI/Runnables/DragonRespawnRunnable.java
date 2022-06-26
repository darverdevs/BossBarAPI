package tech.nully.BossBarAPI.Runnables;

import org.bukkit.scheduler.BukkitRunnable;
import tech.nully.BossBarAPI.BossBar;
import tech.nully.BossBarAPI.SpawnFakeWither;

public class DragonRespawnRunnable extends BukkitRunnable {

    private BossBar bar;
    public DragonRespawnRunnable(BossBar bar) {
        this.bar = bar;
    }
    @Override
    public void run() {
        bar.display();
    }
}
