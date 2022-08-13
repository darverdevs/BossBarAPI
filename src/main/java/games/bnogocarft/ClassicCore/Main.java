package games.bnogocarft.ClassicCore;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }
    public static List<Player> onChatCD = new ArrayList<>();
    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    } 

    // Overrides onDisable
    @Override
    public void onDisable() {

    }
}
