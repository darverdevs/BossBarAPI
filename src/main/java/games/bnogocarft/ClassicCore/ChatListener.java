package games.bnogocarft.ClassicCore;

import com.massivecraft.factions.entity.UPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (Main.onChatCD.contains(e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "ey man chill out 1 message every 1.5 seconds thanks");
            return;
        }
        List<String> arrayMessage = Arrays.asList(e.getMessage().split(" "));
        for (String word : arrayMessage) {
            if (word.contains("nigger") ||
                    word.contains("faggot") ||
                    word.contains("cum") ||
                    word.contains("fag") ||
                    word.contains("nazi") ||
                    word.contains("nig") ||
                    word.contains("beaner") ||
                    word.contains("niggers") ||
                    word.contains("faggots") ||
                    word.contains("fags") ||
                    word.contains("nigga") ||
                    word.contains("niggas") ||
                    word.contains("faggotass") ||
                    word.contains("n1gger") ||
                    word.contains("n1g") ||
                    word.contains("n1gga") ||
                    word.contains("beaners")
            ) {
                arrayMessage.set(arrayMessage.indexOf(word), word.replaceAll("[A-Za-z]", "#"));
            }
        }
        e.setMessage(String.join(" ", arrayMessage));

        Player player = e.getPlayer();
        if (player.hasPermission("bnogorpg.chat.admin")) {
            e.setFormat("" + ChatColor.DARK_GRAY + '[' + ChatColor.DARK_RED + "Admin" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + player.getName() + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + e.getMessage());
            return;
        }

        if (player.hasPermission("bnogorpg.chat.mod")) {
            e.setFormat("" + ChatColor.DARK_GRAY + '[' + ChatColor.DARK_BLUE + "Mod" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_BLUE + player.getName() + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + e.getMessage());
            return;
        }

        if (player.hasPermission("bnogorpg.chat.helper")) {
            e.setFormat("" + ChatColor.DARK_GRAY + '[' + ChatColor.BLUE + "Helper" + ChatColor.DARK_GRAY + ']' + ChatColor.GRAY + ' ' + UPlayer.get(player).getFaction().getName() + ' ' + ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + e.getMessage());
            Main.onChatCD.add(e.getPlayer());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Main.onChatCD.remove(e.getPlayer());
                }
            }, 30L);
            return;
        }

        e.setFormat(ChatColor.GRAY + UPlayer.get(player).getFaction().getName() + ' ' + ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + e.getMessage());
        Main.onChatCD.add(e.getPlayer());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                Main.onChatCD.remove(e.getPlayer());
            }
        }, 30L);
    }
}
