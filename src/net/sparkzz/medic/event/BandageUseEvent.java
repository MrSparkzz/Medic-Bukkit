package net.sparkzz.medic.event;

import net.sparkzz.medic.Main;
import net.sparkzz.util.Colorizer;
import net.sparkzz.util.FileManager;
import net.sparkzz.util.MsgHandler;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BandageUseEvent implements Listener {
	
	FileManager file = Main.getManager();
	MsgHandler msg = MsgHandler.getInstance();
	Colorizer color = Colorizer.getInstance();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBandageUseEvent(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		if (entity instanceof Player) {
			if (player.getItemInHand() == new ItemStack(Material.PAPER)) {
				Player target = (Player) entity;
				
				int heal = file.getConfig().getInt("bandage.heal");
				
				if (target.getHealth() == 20) {
					
				} else if (target.getHealth() < 20 - heal) {
					target.setHealth(target.getHealth() + heal);
					msg.send(player, color.GOLD + target.getDisplayName() + color.GREEN + " is feeling a bit better!");
				} else {
					target.setHealth(20);
					msg.send(player, color.GOLD + target.getDisplayName() + color.GREEN + " has been healed!");
				}
			}
		}
	}
}
