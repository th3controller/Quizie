package net.aemcraftserver.quizie;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class QuizieListener implements Listener {
	
	Quizie plugin;
	
	public QuizieListener(Quizie plugin) {
		this.plugin = plugin;
	}
	private void chatmessage(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	public Location toLocation(String location) {
		String[] loc = location.split(" ");
		try {
			World world = Bukkit.getServer().getWorld(loc[0]);
			int x = Integer.parseInt(loc[1]);
			int y = Integer.parseInt(loc[2]);
			int z = Integer.parseInt(loc[3]);
			return new Location(world, x, y, z);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@EventHandler
	public void PlayerLogin(final PlayerJoinEvent event) {
		if(event.getPlayer().hasPermission("quizie.noob") && !(event.getPlayer().isOp())) {
			final Location loc = toLocation(plugin.getConfig().getString("waypoints.quiz-noob-spawn"));
			if(plugin.people.containsKey(event.getPlayer().getName())) {
				plugin.people.remove(event.getPlayer().getName());
			}
			this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
				public void run() {
					if(event.getPlayer().isOnline()) {
						event.getPlayer().teleport(loc);
						chatmessage(event.getPlayer(), plugin.getConfig().getString("messages.join-message"));
						chatmessage(event.getPlayer(), plugin.getConfig().getString("messages.start-message"));
					}
				}
			}, 40L);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerChat(AsyncPlayerChatEvent event) {
		if(event.getPlayer().hasPermission("quizie.noob.chatdisable") && !(event.getPlayer().isOp())) {
			event.setCancelled(true);
			chatmessage(event.getPlayer(), "&cType &a/quiz start&c to start");
		}
	}
	@EventHandler
	public void PlayerBreak(BlockBreakEvent event) {
		if(event.getPlayer().hasPermission("quizie.noob.break") && !(event.getPlayer().isOp())) {
			event.setCancelled(true);
			chatmessage(event.getPlayer(), "&cType &a/quiz start&c to start");
		}
	}
	@EventHandler
	public void PlayerPlace(BlockPlaceEvent event) {
		if(event.getPlayer().hasPermission("quizie.noob.place") && !(event.getPlayer().isOp())) {
			event.setCancelled(true);
			chatmessage(event.getPlayer(), "&cType &a/quiz start&c to start");
		}
	}
}
