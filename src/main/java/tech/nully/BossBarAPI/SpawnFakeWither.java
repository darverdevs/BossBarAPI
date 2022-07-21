package tech.nully.BossBarAPI;

import com.comphenix.packetwrapper.Packet18SpawnMob;
import com.comphenix.packetwrapper.Packet1DDestroyEntity;
import com.comphenix.packetwrapper.Packet28EntityMetadata;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class SpawnFakeWither extends JavaPlugin {
    public static final int TICKS_PER_SECOND = 20;

    // You could also use a full-fledged API like RemoteEntities
    public static class FakeWither {
        public static final byte INVISIBLE = 0x20;
        // Just a guess
        public static final int HEALTH_RANGE = 80 * 80;
        // Next entity ID
        public static int NEXT_ID = 6000;

        public static final int METADATA_WITHER_HEALTH = 16; // 1.5.2 -> Change to 16

        // Metadata indices
        public static final int METADATA_FLAGS = 0;
        public static final int METADATA_NAME = 5;        // 1.5.2 -> Change to 5
        public static final int METADATA_SHOW_NAME = 6;   // 1.5.2 -> Change to 6

        // Unique ID
        public int id = NEXT_ID++;
        // Default health
        public int health = 300;

        public boolean visible;
        public String customName;
        public boolean created;

        public Location location;
        public ProtocolManager manager;
        public Player p;

        public FakeWither(Player p, ProtocolManager manager) {
            this.location = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 30, p.getLocation().getZ());
            this.manager = manager;
            this.p = p;
        }

        public int getHealth() {
            return health;
        }

        public void setHealth(int health) {
            // Update the health of the entity
            if (created) {
                WrappedDataWatcher watcher = new WrappedDataWatcher();

                watcher.setObject(METADATA_WITHER_HEALTH, (int) health); // 1.5.2 -> Change to (int)
                sendMetadata(watcher);
            }
            this.health = health;
        }

        public void setVisible(boolean visible) {
            // Make visible or invisible
            if (created) {
                WrappedDataWatcher watcher = new WrappedDataWatcher();

                watcher.setObject(METADATA_FLAGS, visible ? (byte) 0 : INVISIBLE);
                sendMetadata(watcher);
            }
            this.visible = visible;
        }

        public void setCustomName(String name) {
            if (created) {
                WrappedDataWatcher watcher = new WrappedDataWatcher();

                if (name != null) {
                    watcher.setObject(METADATA_NAME, name);
                    watcher.setObject(METADATA_SHOW_NAME, (byte) 1);
                } else {
                    // Hide custom name
                    watcher.setObject(METADATA_SHOW_NAME, (byte) 0);
                }

                // Only players nearby when this is sent will see this name
                sendMetadata(watcher);
            }
            this.customName = name;
        }

        public void sendMetadata(WrappedDataWatcher watcher) {
            Packet28EntityMetadata update = new Packet28EntityMetadata();

            update.setEntityId(id);
            update.setEntityMetadata(watcher.getWatchableObjects());
            try {
                manager.sendServerPacket(p, update.getHandle());
            } catch (InvocationTargetException e) {
                Bukkit.getLogger().log(Level.WARNING, "Cannot send " + update.getHandle() + " to " + p, e);
            }
        }

        public int getId() {
            return id;
        }

        public void create() {
            Packet18SpawnMob spawnMob = new Packet18SpawnMob();
            WrappedDataWatcher watcher = new WrappedDataWatcher();

            watcher.setObject(METADATA_FLAGS, visible ? (byte) 0 : INVISIBLE);
            watcher.setObject(METADATA_WITHER_HEALTH, (int) health); // 1.5.2 -> Change to (int)

            if (customName != null) {
                watcher.setObject(METADATA_NAME, customName);
                watcher.setObject(METADATA_SHOW_NAME, (byte) 1);
            }

            spawnMob.setEntityID(id);
            spawnMob.setType(EntityType.ENDER_DRAGON);
            spawnMob.setX(location.getX());
            spawnMob.setY(location.getY());
            spawnMob.setZ(location.getZ());
            spawnMob.setMetadata(watcher);

            try {
                manager.sendServerPacket(p, spawnMob.getHandle());
            } catch (InvocationTargetException e) {
                Bukkit.getLogger().log(Level.WARNING, "Cannot send " + spawnMob.getHandle() + " to " + p, e);
            }
            created = true;
        }

        public void destroy() {
            if (!created)
                throw new IllegalStateException("Cannot kill a killed entity.");

            Packet1DDestroyEntity destroyMe = new Packet1DDestroyEntity();
            destroyMe.setEntities(new int[]{id});

            try {
                manager.sendServerPacket(p, destroyMe.getHandle());
            } catch (InvocationTargetException e) {
                Bukkit.getLogger().log(Level.WARNING, "Cannot send " + destroyMe.getHandle() + " to " + p, e);
            }
            created = false;
        }
    }
}