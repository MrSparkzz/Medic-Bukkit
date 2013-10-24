package net.sparkzz.medic;

import net.sparkzz.medic.command.Commands;
import net.sparkzz.medic.event.BandageUseEvent;
import net.sparkzz.util.FileManager;
import net.sparkzz.util.LogHandler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	LogHandler logger = LogHandler.getInstance();
	
	Commands commands = new Commands();
	
	public static FileManager file = new FileManager();
	
	public static FileManager getManager() {
		return file;
	}
	
	@Override
	public void onDisable() {
		logger.info(this, "Disabled " + this.getName());
	}
	
	@Override
	public void onEnable() {
		commands.initCommands();
		
		boolean bandage = this.getConfig().getBoolean("bandage.enabled");
		
		if (bandage)
			Bukkit.getPluginManager().registerEvents(new BandageUseEvent(), this);
		
		file.setup(this);
		
		logger.info(this, "Enabled " + this.getName());
	}
	
	static Main instance = new Main();
	
	public static Main getInstance() {
		return instance;
	}
}
