package net.sparkzz.medic.command;

import net.sparkzz.command.Register;

public class Commands {
	
	Register register = Register.getRegister();
	
	public void initCommands() {
		register.initCommand("check", new CheckCommand());
		register.initCommand("extinguish", new ExtinguishCommand());
		register.initCommand("feast", new FeastCommand());
		register.initCommand("feed", new FeedCommand());
		register.initCommand("fulfill", new FulfillCommand());
		register.initCommand("heal", new HealCommand());
	}
}
