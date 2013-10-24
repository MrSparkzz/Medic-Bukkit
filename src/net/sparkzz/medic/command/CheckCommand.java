package net.sparkzz.medic.command;

import net.sparkzz.util.Colorizer;
import net.sparkzz.util.MsgHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckCommand implements CommandExecutor {

	MsgHandler msg = MsgHandler.getInstance();
	Colorizer color = Colorizer.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				msg.send(sender, msg.warn("Silly console, you're fine!"));
				return true;
			}
			
			if (!sender.hasPermission("medic.check.self")) {
				msg.deny(sender);
				return true;
			}
			
			Player player = (Player) sender;
			
			int i = checker(player.getHealth());
			int j = checker(player.getFoodLevel());
			
			if (i == 0 && j == 0) {
				msg.send(sender, color.GREEN + "Everything seems to be fine!");
				return true;
			}
			
			if (i == 1 || j == 1) {
				msg.send(sender, color.YELLOW + "You could be better!");
				return true;
			}
			
			if (i == -1 || j == -1) {
				msg.send(sender, color.RED + "You are in bad condition!");
				return true;
			}
		}
		
		if (args.length == 1) {
			if (!sender.hasPermission("medic.check.others")) {
				msg.deny(sender);
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target == null) {
				msg.targetNotFound(sender, args[0]);
				return true;
			}
			
			int i = checker(target.getHealth());
			int j = checker(target.getFoodLevel());
			
			if (i == 0 && j == 0) {
				msg.send(sender, color.GREEN + "Everything seems to be fine!");
				return true;
			}

			if (i == 1 || j == 1) {
				msg.send(sender, color.GOLD + target.getDisplayName() + color.YELLOW + " could be better!");
				return true;
			}
			
			if (i == -1 || j == -1) {
				msg.send(sender, color.GOLD + target.getDisplayName() + color.RED + " is in bad condition!");
				return true;
			}
		}
		
		if (args.length > 1) {
			msg.args(sender, 2);
			return true;
		}
		return false;
	}

	public int checker(double input) {
		int inputLevel = (int) input * 2;
		
		if (inputLevel > 10) return 1;
		if (inputLevel == 20) return 0;
		if (inputLevel < 10) return -1;
		
		return 0;
	}
}