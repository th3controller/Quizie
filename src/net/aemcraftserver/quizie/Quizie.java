package net.aemcraftserver.quizie;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Quizie extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");
	public static Permission perms = null;
	
	public HashMap<String, String> people = new HashMap<String, String>();
	
	@Override
	public void onEnable() {
		setupPermissions();
		File file = new File("plugins/Quizie", "config.yml");
		if(!file.exists()) {
			try {
				this.saveResource("config.yml", true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getServer().getPluginManager().registerEvents(new QuizieListener(this), this);
		log.info("[Quizie] Registering commands...");
		getCommand("quiz").setExecutor(new CmdRandom(this));
		getCommand("answer").setExecutor(new CmdAnswer(this));
		log.info("[Quizie] Registered commands!");
		log.info("[Quizie] Enabled!");
	}
	@Override
	public void onDisable() {
		log.info("[Quizie] Disabled!");
	}
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
