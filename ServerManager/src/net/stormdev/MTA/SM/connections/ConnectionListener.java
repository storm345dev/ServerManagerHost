package net.stormdev.MTA.SM.connections;

import java.net.ServerSocket;
import java.net.Socket;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.utils.Scheduler;

public class ConnectionListener {
	
	private int port;
	
	public ConnectionListener(int port){
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
			if(!socket.isBound()){
				Core.logger.error("Socket failed to bind!");
			}
			Core.logger.info("Listening to packets!");
			while(Main.running){
				Socket clientSocket = socket.accept();
				clientSocket.setKeepAlive(true);
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
