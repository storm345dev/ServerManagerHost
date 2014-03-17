package net.stormdev.MTA.SM.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.stormdev.MTA.SM.connections.ConnectionListener;
import net.stormdev.MTA.SM.connections.ConnectionManager;
import net.stormdev.MTA.SM.events.CommandInputEvent;
import net.stormdev.MTA.SM.events.EventManager;
import net.stormdev.MTA.SM.messaging.Encrypter;
import net.stormdev.MTA.SM.messaging.MessageListener;
import net.stormdev.MTA.SM.utils.Scheduler;

public class Main {
	
	private int port = 0; //50k
	private String passPhrase;
	
	public static volatile boolean running = false;
	public volatile boolean ending = false;
	
	public ConnectionListener connectionListener;
	public ConnectionManager connections;
	public EventManager eventManager;
	public Encrypter encrypter;
	
	private String[] args;
	
	public Main(String[] args){
		this.args = args;
		running = true;
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
				return;
			}
		});
		
		start();
		return;
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
		return;
	}
	
	public boolean onLoad(String[] args){
		Core.logger.info("Loading...");
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
		try {
			passPhrase = new String(args[1].getBytes(), "UTF-16");
		} catch (UnsupportedEncodingException e1) {
			// Uh oh
			Core.logger.error("Pass phrase MUST be in UTF-16!");
			return false;
		}
		passPhrase = passPhrase.trim();
		encrypter = new Encrypter(passPhrase);
		if(!encrypter.test()){
			Core.logger.error("Invalid security key!");
			return false;
		}
		
		Core.logger.info("Using options: Port: '"+port+"' PassPhrase: '"+passPhrase+"'");
		
		new Scheduler(); //Initialize it
		
		eventManager = new EventManager();
		new MessageListener();
		
		connections = new ConnectionManager();
		connectionListener = new ConnectionListener(port);
		
		Core.logger.info("Running!");
		
		Scheduler.instance.runTaskAsync(new Runnable(){

			public void run() {
				new InputListener();
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				while(running){
					try {
						String line;
						while(running && (line = br.readLine()) != null){
							try {
								CommandInputEvent event = new CommandInputEvent(line);
								eventManager.callEvent(event);
								if(line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("end")){
									return;
								}
							} catch (Exception e) {
								// Proceed with reading
								e.printStackTrace();
							}
						}
					} catch (IOException e) {
						continue; //error
					}
				}
				try {
					br.close();
				} catch (IOException e) {
					return;
				}
				return;
			}});
		
		return true;
	}
	
	public void shutdown(){
		running = false;
		if(!ending){
			end();
		}
		ending = true;
		return;
	}
	
	public void end(){
		if(ending){
			return;
		}
		
		Scheduler.instance.shutdown();
		connections.closeAll();
		connectionListener.close();
		ending = true;
		running = false;
	}
	
	public void onTick(){
		Scheduler.instance.pullQueue();
	}
}
