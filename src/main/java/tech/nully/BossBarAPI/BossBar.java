package tech.nully.BossBarAPI;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BossBar {
    private int bossHealth = 200;
    private String text = "A BossBar!";

    private SpawnFakeWither.FakeWither dragon;

    private Player p;

    public BossBar(Player p) {
        this.p = p;
    }

    public int getHealth() {
        return bossHealth;
    }

    public void setHealth(int bossHealth) {
        this.bossHealth = bossHealth;
        if (dragon != null) {
            if (dragon.created) {
                dragon.setHealth(bossHealth);
            }
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (dragon != null) {
            if (dragon.created) {
                dragon.setCustomName(text);
            }
        }
    }

    public void display() {
        if (dragon != null) {
            if (dragon.created) {
                dragon.destroy();
            }
        }
        dragon = new SpawnFakeWither.FakeWither(
                new Location(p.getWorld(), p.getLocation().getX(), -15, p.getLocation().getZ()), ProtocolLibrary.getProtocolManager());
        dragon.setCustomName(text);
        dragon.setVisible(false);
        dragon.create();
    }

    public void delete() {
        if (dragon != null) {
            if (dragon.created) {
                dragon.destroy();
            }
        }
    }
}
