package tech.nully.BossBarAPI;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import tech.nully.BossBarAPI.Runnables.DragonDeleteRunnable;
import tech.nully.BossBarAPI.Runnables.DragonRespawnRunnable;

import static tech.nully.BossBarAPI.SpawnFakeWither.TICKS_PER_SECOND;

public class BossBar {
    private int bossHealth = 200;
    private String text = "A BossBar!";

    private SpawnFakeWither.FakeWither dragon;

    private Player p;

    public BossBar(Player p) {
        this.p = p;
    }
    private DragonRespawnRunnable respawnRunnable;

    public int getHealth() {
        return bossHealth;
    }

    public void setHealth(int bossHealth) {
        this.bossHealth = bossHealth;
        if (dragon != null) {
            dragon.setHealth(bossHealth);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (dragon != null) {
            dragon.setCustomName(text);
        }
    }

    public void display() {
        if (dragon != null) {
            dragon.destroy();
        }
        dragon = new SpawnFakeWither.FakeWither(
                new Location(p.getWorld(), p.getLocation().getX(), -15, p.getLocation().getZ()), ProtocolLibrary.getProtocolManager());
        dragon.setCustomName(text);
        dragon.setVisible(false);
        dragon.create();

        respawnRunnable = new DragonRespawnRunnable(this);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new DragonDeleteRunnable(dragon), 1);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), respawnRunnable, TICKS_PER_SECOND*4, TICKS_PER_SECOND*4);
    }

    public void delete() {
        if (dragon != null) {
            dragon.destroy();

            if (respawnRunnable != null) {
                respawnRunnable.cancel();
            }
        }
    }
}
