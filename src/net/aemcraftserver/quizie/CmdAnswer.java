package net.aemcraftserver.quizie;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CmdAnswer implements CommandExecutor {

	Quizie plugin;
	
	public CmdAnswer(Quizie plugin) {
		this.plugin = plugin;
	}
	private void cm(Player player, String message) {
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
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Commands cannot be used from the console");
			return true;
		} else {
			if(args.length == 1) {
				FileConfiguration getCon = plugin.getConfig();
				Player player = (Player)sender;
				if(args[0].equalsIgnoreCase(getCon.getString("quiz."+plugin.people.get(player.getName())+".answer"))) {
					if(!(player.hasPermission("quizie.noob.answer"))) {
						cm(player, getCon.getString("messages.unknown-command"));
					} else {
						if(getCon.getString("quiz."+plugin.people.get(player.getName())+".nextquiz").equals("finish")) {
							Location loc = toLocation(plugin.getConfig().getString("waypoints.finished-spawn"));
							Quizie.perms.playerAddGroup(player, getCon.getString("permissions.group-to-promoted-to"));
							Quizie.perms.playerRemoveGroup(player, getCon.getString("permissions.group-from"));
							cm(player, "&aCongratulations you have finished the questionaire!");
							cm(player, "&aYou can now roam around the Greylist World!");
							cm(player, "&aMake sure to &c/vote&a to earn free stuff!");
							player.teleport(loc);
						} else {
							cm(player, "&aYour answer is correct!");
							plugin.people.put(player.getName(), getCon.getString("quiz."+plugin.people.get(player.getName())+".nextquiz"));
							cm(player, getCon.getString("quiz."+plugin.people.get(player.getName())+".question"));
							cm(player, getCon.getString("quiz."+plugin.people.get(player.getName())+".choices"));
						}
					}
				} else {
					if(plugin.people.containsKey(player.getName())) {
						plugin.people.remove(player.getName());
					}
					cm(player, "&cIncorrect answer, type &a/quiz start&c to start over");
				}
			} else {
				Player player = (Player)sender;
				cm(player, plugin.getConfig().getString("messages.unknown-command"));
			}
			return true;
		}
	}
}
