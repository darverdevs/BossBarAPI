package tech.nully.BossBarAPI;

import com.comphenix.packetwrapper.Packet3ENamedSoundEffect;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.server.v1_5_R3.Packet62NamedSoundEffect;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        System.out.println("BossBar is on");
    } 

    // Overrides onDisable
    @Override
    public void onDisable() {

    }
}
