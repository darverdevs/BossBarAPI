package tech.nully.BossBarAPI;

import com.comphenix.packetwrapper.Packet18SpawnMob;
import com.comphenix.packetwrapper.Packet1DDestroyEntity;
import com.comphenix.packetwrapper.Packet28EntityMetadata;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.PlayerLoggedOutException;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class SpawnFakeWither extends JavaPlugin {
    static final int TICKS_PER_SECOND = 20;

    // You could also use a full-fledged API like RemoteEntities
    static class FakeWither {
        public static final byte INVISIBLE = 0x20;
        // Just a guess
        private static final int HEALTH_RANGE = 80 * 80;
        // Next entity ID
        private static int NEXT_ID = 6000;

        private static final int METADATA_WITHER_HEALTH = 16; // 1.5.2 -> Change to 16

        // Metadata indices
        private static final int METADATA_FLAGS = 0;
        private static final int METADATA_NAME = 5;        // 1.5.2 -> Change to 5
        private static final int METADATA_SHOW_NAME = 6;   // 1.5.2 -> Change to 6

        // Unique ID
        private int id = NEXT_ID++;
        // Default health
        private int health = 300;

        private boolean visible;
        private String customName;
        boolean created;

        private Location location;
        private ProtocolManager manager;

        private Player p;

        public FakeWither(Player p, ProtocolManager manager) {
            this.p = p;
            this.location = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 25, p.getLocation().getZ());
            this.manager = manager;
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

                watcher.setObject(METADATA_FLAGS, visible ? (byte)0 : INVISIBLE);
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

        private void sendMetadata(WrappedDataWatcher watcher) {
            Packet28EntityMetadata update = new Packet28EntityMetadata();

            update.setEntityId(id);
            update.setEntityMetadata(watcher.getWatchableObjects());
            sendPacket(update.getHandle(), p);
        }

        public int getId() {
            return id;
        }

        public void create() {
            Packet18SpawnMob spawnMob = new Packet18SpawnMob();
            WrappedDataWatcher watcher = new WrappedDataWatcher();

            watcher.setObject(METADATA_FLAGS, visible ? (byte)0 : INVISIBLE);
            watcher.setObject(METADATA_WITHER_HEALTH, (int) health); // 1.5.2 -> Change to (int)

            if (customName != null) {
                watcher.setObject(METADATA_NAME, customName);
                watcher.setObject(METADATA_SHOW_NAME, (byte) 1);
            }

            spawnMob.setEntityID(id);
            spawnMob.setType(EntityType.WITHER);
            spawnMob.setX(location.getX());
            spawnMob.setY(location.getY());
            spawnMob.setZ(location.getZ());
            spawnMob.setMetadata(watcher);

            sendPacket(spawnMob.getHandle(), p);
            created = true;
        }

        public void destroy() {
            if (!created)
                throw new IllegalStateException("Cannot kill a killed entity.");

            Packet1DDestroyEntity destroyMe = new Packet1DDestroyEntity();
            destroyMe.setEntities(new int[] { id });

            sendPacket(destroyMe.getHandle(), p);
            created = false;
        }

        private void sendPacket(PacketContainer packet, Player p) {
            try {
                manager.sendServerPacket(p, packet);
            } catch (InvocationTargetException e) {
                Bukkit.getLogger().log(Level.WARNING, "Cannot send " + packet + " to " + p, e);
            } catch (PlayerLoggedOutException ignore) {}
        }
    }
}