package tech.nully.BossBarAPI.Runnables;

import org.bukkit.scheduler.BukkitRunnable;
import tech.nully.BossBarAPI.SpawnFakeWither;

public class DragonDeleteRunnable extends BukkitRunnable {

    private SpawnFakeWither.FakeWither dragon;
    public DragonDeleteRunnable(SpawnFakeWither.FakeWither dragon) {
        this.dragon = dragon;
    }
    @Override
    public void run() {
        dragon.destroy();
    }
}
