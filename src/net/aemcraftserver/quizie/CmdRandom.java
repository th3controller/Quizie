package net.aemcraftserver.quizie;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CmdRandom implements CommandExecutor {
	
	Quizie plugin;
	
	public CmdRandom(Quizie plugin) {
		this.plugin = plugin;
	}
	private void cm(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Commands cannot be used from the console");
			return true;
		} else {
			if(args.length == 1) {
				FileConfiguration getCon = plugin.getConfig();
				Player player = (Player)sender;
				if(args[0].equalsIgnoreCase("start")) {
					if(!(player.hasPermission("quizie.noob.start"))) {
						cm(player, plugin.getConfig().getString("messages.unknown-command"));
					} else {
						if(plugin.people.containsKey(player.getName())) {
							cm(player, "&cYou already started the quiz!");
						} else {
							plugin.people.put(player.getName(), getCon.getString("quiz.startingquiz"));
							cm(player, getCon.getString("quiz."+plugin.people.get(player.getName())+".question"));
							cm(player, getCon.getString("quiz."+plugin.people.get(player.getName())+".choices"));
						}
					}
				}
				else if(args[0].equalsIgnoreCase("setnoobspawn")) {
					if(!(player.hasPermission("quizie.admin.setspawn"))) {
						cm(player, plugin.getConfig().getString("messages.unknown-command"));
					} else {
						String world = player.getWorld().getName();
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY();
						int z = player.getLocation().getBlockZ();
						getCon.set("waypoints.quiz-noob-spawn", world+" "+x+" "+y+" "+z);
						plugin.saveConfig();
						plugin.reloadConfig();
						cm(player, "&aSuccessfully created a waypoint for quiz taking players");
					}
				}
				else if(args[0].equalsIgnoreCase("setspawn")) {
					if(!(player.hasPermission("quizie.admin.setspawn"))) {
						cm(player, plugin.getConfig().getString("messages.unknown-command"));
					} else {
						String world = player.getWorld().getName();
						int x = player.getLocation().getBlockX();
						int y = player.getLocation().getBlockY();
						int z = player.getLocation().getBlockZ();
						getCon.set("waypoints.finished-spawn", world+" "+x+" "+y+" "+z);
						plugin.saveConfig();
						plugin.reloadConfig();
						cm(player, "&aSuccessfully created a waypoint for players who have finished the quiz");
					}
				}
			} else {
				Player player = (Player)sender;
				cm(player, plugin.getConfig().getString("messages.unknown-command"));
			}
			return true;
		}
	}
}
