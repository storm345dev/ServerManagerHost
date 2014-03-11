package net.stormdev.MTA.SM.connections;

import java.net.ServerSocket;
import java.net.Socket;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.utils.Scheduler;

public class ConnectionListener {
	
	private Main main;
	private int port;
	
	public ConnectionListener(int port){
		this.main = Core.instance;
		this.port = port;
		Scheduler.instance.runTaskAsync(new Runnable(){

			public void run() {
				open();
				return;
			}});
	}
	
	public void open(){
		try {
			Core.logger.info("Binding to port...");
			ServerSocket socket = new ServerSocket(port);
			Core.logger.info("Listening to packets!");
			while(Main.running){
				Socket clientSocket = socket.accept();
				ConnectionInterpreter reader = new ConnectionInterpreter(clientSocket);
				reader.start();
			}
			if(!Main.running){
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(Main.running){
				open();
				return;
			}
		}
		return;
	}
}
