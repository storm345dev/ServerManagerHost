package net.stormdev.MTA.SM.core;

import net.stormdev.MTA.SM.connections.ConnectionListener;
import net.stormdev.MTA.SM.connections.Message;
import net.stormdev.MTA.SM.events.EventManager;
import net.stormdev.MTA.SM.messaging.Encrypter;
import net.stormdev.MTA.SM.messaging.MessageEvent;
import net.stormdev.MTA.SM.messaging.MessageListener;
import net.stormdev.MTA.SM.utils.Scheduler;

public class Main {
	
	private int port = 50000; //50k
	private String passPhrase;
	
	public static boolean running = true;
	private boolean ending = false;
	private ConnectionListener connectionListener;
	public EventManager eventManager;
	public Encrypter encrypter;
	
	private String[] args;
	
	public Main(String[] args){
		this.args = args;
	}
	
	public void begin(){
		if(!onLoad(args)){
			System.exit(0);
			return; //Terminate the program
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			
			@Override
			public void run(){
				if(!ending){
					end();
				}
				System.exit(0);
				return;
			}
		});
		
		start();
	}
	
	public void start(){
		//Start everything
				while(running){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						if(!ending){
							end();
						}
						return; //Stop program
					}
					onTick();
				}
	}
	
	public boolean onLoad(String[] args){
		Core.logger.info("Loading...");
		//TODO Servermanager
		if(args.length < 2){
			Core.logger.error("Sorry, not enough args, please launch the program with the port you wish to use and the security passphrase!");
			return false;
		}
		String rawPort = args[0];
		try {
			port = Integer.parseInt(rawPort);
		} catch (NumberFormatException e) {
			Core.logger.error("Sorry, you must launch the program with the port you wish to use!");
			return false;
		}
		passPhrase = args[1];
		encrypter = new Encrypter(passPhrase);
		if(!encrypter.test()){
			Core.logger.error("Invalid security key!");
			return false;
		}
		
		new Scheduler(); //Initialize it
		connectionListener = new ConnectionListener(port);
		eventManager = new EventManager();
		
		new MessageListener();
		
		Core.logger.info("Running!");
		
		return true;
	}
	
	public void end(){
		if(ending){
			return;
		}
		
		Scheduler.instance.shutdown();
		ending = false;
		running = false;
	}
	
	public void onTick(){
		Scheduler.instance.pullQueue();
	}
}
