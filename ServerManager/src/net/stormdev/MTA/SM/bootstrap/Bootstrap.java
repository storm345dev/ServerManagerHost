package net.stormdev.MTA.SM.bootstrap;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;


public class Bootstrap {//TODO Initialise SMHost correctly so it supports reloading...
	private static Application app;
	
	public static void onEnable(Application app, int port, String pass){
		Bootstrap.app = app;
		Core.logger = app;
		Core.logger.info("Picked up boot options: Port: '"+port+"' PassCode: '"+pass+"'");
		Main main = new Main(new String[]{""+port, pass}, true);
		Core.instance = main;
		main.begin();
	}
	
	public static void onDisable(){
		Core.logger.info("Attempting to terminate NSM...");
		Core.instance.shutdown();
	}
}