package net.stormdev.MTA.SM.connections;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.utils.Scheduler;

public class ConnectionReader implements Runnable {

	private volatile Socket socket;
	private volatile Main main;
	private volatile boolean open = false;
	private volatile BufferedReader inFromClient = null;
	private volatile PrintWriter out = null;
	
	public ConnectionReader(Socket socket){
		this.socket = socket;
		this.main = Core.instance;
	}
	
	public void close(){
		try {
			if(out != null){
				out.close();
			}
			if(inFromClient != null){
				inFromClient.close();
			}
		} catch (IOException e) {
			//Whatever
		}
		open = false;
	}
	
	public void start(){
		if(!isOpen()){
			connect();
		}
		Scheduler.instance.runTaskAsync(this);
	}
	
	public void connect(){
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
			open = true;
		} catch (IOException e) {
			e.printStackTrace();
			open = false;
		}
	}
	
	public void run() {
		// Run it
		try {
			if(open && socket.isConnected() && !socket.isClosed()){
				out.println("identify");
				out.flush();
			}
			
			while(open && socket.isConnected() && !socket.isClosed()){
				//TODO We are listening
				String line;
				while((line = inFromClient.readLine()) != null){
					//TODO Recieved message : line
					if(line.equalsIgnoreCase("close")){
						close();
						return;
					}
					else if(line.equalsIgnoreCase("ping")){
						rawMsg("pong");
						continue;
					}
					//TODO Manage normal msgs
				}
			}
			open = false;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		return;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public void rawMsg(String msg){
		if(!isOpen()){
			return;
		}
		out.println(msg);
		out.flush();
	}
	
}
