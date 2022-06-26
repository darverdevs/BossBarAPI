package tech.nully.BossBarAPI;

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
        getCommand("bossbar").setExecutor(new FakeWitherCommand());
    }

    // Overrides onDisable
    @Override
    public void onDisable() {

    }
}
