package net.sparkzz.medic.command;

import net.sparkzz.medic.Main;
import net.sparkzz.util.Colorizer;
import net.sparkzz.util.Cooldowns;
import net.sparkzz.util.FileManager;
import net.sparkzz.util.MsgHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
	
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
			
			if (!sender.hasPermission("medic.heal.self")) {
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
			
			boolean cooldown = file.getConfig().getBoolean("cooldowns.heal.enabled");
			Long delay = (long) (file.getConfig().getInt("cooldowns.heal.time") * 1000);
			
			if (cooldown && !player.hasPermission("medic.cooldown.healbypass") && (delay != null || delay != 0))
				if (Cooldowns.tryCooldown(player, "healCooldown", delay)) {
					
				} else {
					int remaining = (int) (Cooldowns.getCooldown(player, "healCooldown") / 1000);
					
					msg.send(player, color.RED + "You still have " + remaining + " seconds left until you can use this again!");
					return true;
				}
			
			player.setHealth(20);
			msg.send(player, color.GREEN + "You have been healed!");
			return true;
		}
		
		if (args.length == 1) {
			if (!sender.hasPermission("medic.heal.others")) {
				msg.deny(sender);
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target == null) {
				msg.targetNotFound(sender, args[0]);
				return true;
			}
			
			if (target.isDead()) {
				msg.send(sender, color.GOLD + target.getDisplayName() + color.RED + " is dead!");
				return true;
			}
			
			if (target.getHealth() == 20) {
				msg.send(sender, color.GOLD + target.getDisplayName() + color.RED + "'s health is already full!");
				return true;
			}

			boolean cooldown = file.getConfig().getBoolean("cooldowns.heal.enabled");
			Long delay = (long) (file.getConfig().getInt("cooldowns.heal.time") * 1000);
			
			if (cooldown && !sender.hasPermission("medic.cooldown.healbypass") && (delay != null || delay != 0))
				if (Cooldowns.tryCooldown((Player) sender, "healCooldown", delay)) {
					
				} else {
					int remaining = (int) (Cooldowns.getCooldown((Player) sender, "healCooldown") / 1000);
					
					msg.send(sender, color.RED + "You still have " + remaining + " seconds left until you can use this again!");
					return true;
				}
			
			target.setHealth(20);
			msg.send(sender, color.GOLD + target.getDisplayName() + color.GREEN + " has been healed!");
			msg.send(target, color.GREEN + "You have been healed!");
			return true;
		}
		
		if (args.length > 1) {
			msg.args(sender, 2);
			return true;
		}
		return false;
	}
}