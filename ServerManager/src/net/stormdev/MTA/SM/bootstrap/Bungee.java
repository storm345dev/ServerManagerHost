package net.stormdev.MTA.SM.bootstrap;

import net.md_5.bungee.api.plugin.Plugin;

public class Bungee extends Plugin {
	@Override
	public void onEnable(){
		int port = 50000;
		String pass = "pass";
		
		Bootstrap.onEnable(port, pass);
	}
	
	@Override
	public void onDisable(){
		Bootstrap.onDisable();
	}
}
