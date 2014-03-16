package net.stormdev.MTA.SM.connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.utils.Scheduler;

public class ConnectionListener {
	
	private int port;
	private ServerSocket socket;
	
	public ConnectionListener(int port){
		this.port = port;
		Scheduler.instance.runTaskAsync(new Runnable(){

			public void run() {
				open();
				return;
			}});
	}
	
	public void close(){
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				// Oh dear
				e.printStackTrace();
			}
		}
	}
	
	public void open(){
		try {
			Core.logger.info("Binding to port...");
			socket = new ServerSocket(port);
			if(!socket.isBound()){
				Core.logger.error("Socket failed to bind!");
			}
			Core.logger.info("Listening to packets!");
			while(Main.running){
				try {
					Socket clientSocket;
					try {
						clientSocket = socket.accept();
					} catch (Exception e) {
						return; //Program has ended
					}
					clientSocket.setKeepAlive(true);
					ConnectionInterpreter reader = new ConnectionInterpreter(clientSocket);
					reader.start();
				} catch (Exception e) {
					//Whatever
				}
			}
			socket.close();
			if(!Main.running){
				return;
			}
		} catch (Exception e) {
			Core.logger.error("Critical error! Is port "+port+" already in use?");
			Core.instance.shutdown();
			return;
		}
		return;
	}
}
