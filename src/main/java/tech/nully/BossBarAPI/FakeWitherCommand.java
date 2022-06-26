package tech.nully.BossBarAPI;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import static tech.nully.BossBarAPI.SpawnFakeWither.TICKS_PER_SECOND;

public class FakeWitherCommand implements CommandExecutor {

    private BukkitTask task;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            BossBar bar = new BossBar(player);
            bar.setText("Countdown");


            // Count down
            task = Bukkit.getServer().getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    // Count down
                    bar.setHealth(bar.getHealth() - 1);

                    if (bar.getHealth() <= 0) {
                        bar.delete();
                        task.cancel();
                    }
                }
            }, TICKS_PER_SECOND / 4, TICKS_PER_SECOND / 4);
        }
        return true;
    }
}