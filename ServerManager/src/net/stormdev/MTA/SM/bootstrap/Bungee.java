package net.stormdev.MTA.SM.bootstrap;

import java.io.IOException;
import java.util.logging.Level;

import net.md_5.bungee.api.plugin.Plugin;
import net.stormdev.MTA.SM.core.Core;
import configuration.InvalidConfigurationException;

public class Bungee extends Plugin implements Application {
	
	private BootConfig boot;
	private int port = 50000;
	private String pass = "pass";
	
	@Override
	public void onEnable(){
		try {
			boot = new BootConfig(getDataFolder());
		} catch (IOException e) {
			getLogger().info("Unable to read/write boot config!");
			return;
		} catch (InvalidConfigurationException e) {
			getLogger().info("Invalid boot config!");
			return;
		}
		try {
			port = (Integer) ConfigSettings.PORT.get();
			pass = (String) ConfigSettings.PASS.get();
		} catch (Exception e) {
			getLogger().info("Invalid boot config!");
			return;
		}
		log("Reading boot config for args to pass to SMHost...");
		
		final Application app = this;
		new Thread(){
			@Override
			public void run(){
				Bootstrap.onEnable(app, port, pass);
				return;
			}
		}.start();
	}
	
	@Override
	public void onDisable(){
		Bootstrap.onDisable();
	}

	public void handleUpdater() {
		// TODO When updater is a thing
	}

	public void log(String msg, Level level) {
		getLogger().log(level, "NSM: "+msg);
	}

	public void log(String msg) {
		getLogger().log(Level.INFO, "NSM: "+msg);
	}

	public void debug(String msg, Level level) {
		if(Core.debug){
			getLogger().log(level, "NSM: "+msg);
		}
	}

	public void debug(String msg) {
		if(Core.debug){
			getLogger().info("NSM: "+msg);
		}
	}

	public void info(String msg) {
		getLogger().info("NSM: "+msg);
	}

	public void info(String msg, Level level) {
		getLogger().log(level,"NSM: "+ msg);
	}

	public void error(String msg) {
		getLogger().warning("NSM: "+msg);
	}

	public void error(String msg, Level level) {
		getLogger().log(level, "NSM: "+msg);
	}

	public void error(String msg, Level level, Exception error) {
		getLogger().log(level, "NSM: "+msg);
		error.printStackTrace();
	}

	public void error(String msg, Exception error) {
		getLogger().log(Level.SEVERE, "NSM: "+msg);
		error.printStackTrace();
	}

	public void error(Level level, Exception error) {
		getLogger().log(Level.SEVERE, "NSM: "+"An error has occured!");
		error.printStackTrace();
	}

	public void warning(String msg) {
		getLogger().warning("NSM: "+msg);
	}

	public void warning(String msg, Level level) {
		getLogger().log(level, "NSM: "+msg);
	}
}
