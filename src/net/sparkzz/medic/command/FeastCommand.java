package net.sparkzz.medic.command;

import net.sparkzz.medic.Main;
import net.sparkzz.util.Colorizer;
import net.sparkzz.util.Cooldowns;
import net.sparkzz.util.FileManager;
import net.sparkzz.util.MsgHandler;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeastCommand implements CommandExecutor {

	FileManager file = Main.getManager();
	MsgHandler msg = MsgHandler.getInstance();
	Colorizer color = Colorizer.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				msg.send(sender, msg.warn("Silly console, you don't need healing!"));
				return true;
			}
			
			if (!sender.hasPermission("medic.feast")) {
				msg.deny(sender);
				return true;
			}
			
			Player player = (Player) sender;
			
			if (player.isDead()) {
				msg.send(sender, msg.warn("You are dead!"));
				return true;
			}
			
			if (player.getHealth() == 20) {
				msg.send(sender, msg.warn("Your health is already full!"));
				return true;
			}
			
			if (player.getFoodLevel() == 0) {
				msg.send(sender, msg.warn("You are starving!"));
				return true;
			}
			
		    World world = player.getWorld();
		    Sound sound = Sound.EAT;
			
			int refill = 2;
			
			boolean cooldown = file.getConfig().getBoolean("cooldowns.feast.enabled");
			Long delay = (long) (file.getConfig().getInt("cooldowns.feast.time") * 1000);
		
			
			if (cooldown && !sender.hasPermission("medic.cooldown.feastbypass") && (delay != null || delay != 0))
				if (Cooldowns.tryCooldown((Player) sender, "feastCooldown", delay)) {
					
				} else {
					int remaining = (int) (Cooldowns.getCooldown((Player) sender, "feastCooldown") / 1000);
					
					msg.send(sender, color.RED + "You still have " + remaining + " seconds left until you can use this again!");
					return true;
				}
			
			if (!(player.getHealth() <= 20 - refill) && (player.getFoodLevel() - refill != 0)) {
			    world.playSound(player.getLocation(), sound, 5, 1);
				
			    player.setHealth(20);
			    player.setFoodLevel(player.getFoodLevel() - refill);
				
			    msg.send(sender, color.GREEN + "You are now fully healed!");
				return true;
			}
			
			if (player.getHealth() <= 20 - refill && (player.getFoodLevel() - refill != 0)) {
				world.playSound(player.getLocation(), sound, 5, 1);
				
				player.setHealth(player.getHealth() + refill);
				player.setFoodLevel(player.getFoodLevel() - refill);
				
				msg.send(sender, color.GREEN + "Nom, nom, nom!");
				return true;
			}
			
			msg.send(sender, msg.warn("You're starving!"));
			return true;
		} else {
			msg.args(sender, 0);
			return true;
		}
	}
}
