package tech.nully.BossBarAPI;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BossBar {
    private int bossHealth = 300;
    private String text = "A BossBar!";

    private SpawnFakeWither.FakeWither wither;

    private Player p;

    public BossBar(Player p) {
        this.p = p;
    }

    public int getHealth() {
        return bossHealth;
    }

    private TeleportScheduler t;

    public void setHealth(int bossHealth) {
        this.bossHealth = bossHealth;
        if (wither != null) {
            if (wither.created) {
                wither.setHealth(bossHealth);
            }
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (wither != null) {
            if (wither.created) {
                wither.setCustomName(text);
            }
        }
    }

    public void display() {
        if (wither != null) {
            if (wither.created) {
                wither.destroy();
            }
        }
        wither = new SpawnFakeWither.FakeWither(p, ProtocolLibrary.getProtocolManager());
        wither.setCustomName(text);
        wither.setVisible(false);
        wither.create();

        t = new TeleportScheduler(this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), t, 100);
    }

    public void delete() {
        if (wither != null) {
            if (wither.created) {
                if (t != null) {
                    t.cancel();
                }
                wither.destroy();
            }
        }
    }
}
