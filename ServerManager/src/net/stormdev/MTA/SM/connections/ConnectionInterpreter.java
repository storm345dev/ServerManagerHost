package net.stormdev.MTA.SM.connections;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.messaging.MessageEvent;
import net.stormdev.MTA.SM.messaging.MessageRecipient;
import net.stormdev.MTA.SM.utils.Scheduler;

public class ConnectionInterpreter implements Runnable {

	private static final String serverIdentify = "server";
	private static final String clientIdentify = "client";
	private static final boolean useSendQueue = false;
	
	private volatile Socket socket;
	private volatile boolean open = false;
	private volatile BufferedReader inFromClient = null;
	private volatile PrintWriter out = null;
	
	private volatile String id = null;
	private volatile boolean indentified = false;
	private volatile long startTime;
	private volatile long lastMessage = 0;
	private volatile Connection connection;
	
	private volatile List<TransitMessage> inbound = new ArrayList<TransitMessage>();
	private volatile List<String> outboundQueue = new ArrayList<String>();
	
	public ConnectionInterpreter(Socket socket){
		this.socket = socket;
	}
	
	public Connection getConnection(){
		return connection;
	}
	
	public boolean isIdentified(){
		return indentified;
	}
	
	public String getConnectionId(){
		return id;
	}
	
	public long getConnectStartTimeMillis(){
		return startTime;
	}
	
	public long getConnectionDurationMillis(){
		if(startTime < 1){
			return 0; //Not connected
		}
		return System.currentTimeMillis() - startTime;
	}
	
	public void close(){
		try {
			if(out != null){
				out.close();
			}
			if(inFromClient != null){
				inFromClient.close();
			}
			if(inFromClient != null){
				inFromClient.close();
			}
			if(isIdentified()){
				Core.instance.connections.unregisterConnection(id);
			}
		} catch (IOException e) {
			// Whatever
			e.printStackTrace();
		}
		finally {
			open = false;
		}
	}
	
	public void start(){
		if(!isOpen()){
			connect();
		}
		Scheduler.instance.runTaskAsync(this);
		Scheduler.instance.runTaskAsync(new Runnable(){

			@SuppressWarnings("unused")
			public void run() {
				int i = 0;
				while(Main.running && open && socket.isConnected() && !socket.isClosed()){
					
					if(i < 1){ //1 time in a 40 cycle, so roughly every 5s
					sendKeepAliveMessage();
					checkIfAlive();
					}
					
					if(!indentified){
						long diff = System.currentTimeMillis() - startTime;
						if(diff > 10000){ //10s since we asked to identify and no response; so just terminate the connection
							rawMsg("close");
							close();
							return;
						}
					}

					if(useSendQueue && outboundQueue.size() > 0){
						List<String> toSend = new ArrayList<String>(outboundQueue);
						for(String data:toSend){
							rawMsg(data);
							outboundQueue.remove(data);
						}
					}
					
					i++;
					if(i>40){
						i = 0;
					}
					try {
						Thread.sleep(125);//1/8s
					} catch (InterruptedException e) {
					} 
				}
				if(isOpen()){
					close();
				}
				return;
			}});
	}
	
	public void connect(){
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
			lastMessage = System.currentTimeMillis();
			open = true;
		} catch (IOException e) {
			e.printStackTrace();
			open = false;
		}
	}
	
	public boolean checkIfAlive(){
		if(!isOpen()){
			return false;
		}
		long diff = System.currentTimeMillis() - lastMessage;
		if(diff > 10000){
			//It has timed out
			rawMsg("close");
			close();
			return false;
		}
		return true;
	}
	
	public void run() {
		startTime = System.currentTimeMillis();
		if(open && socket.isConnected() && !socket.isClosed()){
			out.println("identify");
			out.flush();
		}
		
		while(open && socket.isConnected() && !socket.isClosed()){
			//We are listening
			try {
				String line;
				while((line = inFromClient.readLine()) != null){
					lastMessage = System.currentTimeMillis(); //Make sure we know it's still responsive
					//Recieved message : line
					if(line.equalsIgnoreCase("close")){
						close();
						return;
					}
					else if(line.equalsIgnoreCase("ping")){
						rawMsg("pong");
						continue;
					}
					else if(line.equalsIgnoreCase("alive")){
						//Don't need to manage, it's just saying it's alive
						continue;
					}
					
					Message received = processMsg(line);
					if(received != null){ //Message recieved!
						if(received.getTo().equalsIgnoreCase(MessageRecipient.HOST.getConnectionID()) && received.getMsgTitle().equals("indentify")){ //They are identifying with us
							String msg = received.getMsg();
							if(!msg.equals(serverIdentify) && !msg.equals(clientIdentify)){
								rawMsg("badSecurityCode");
								rawMsg("close");
								close();
								return;
							}
							boolean server = msg.equals(serverIdentify); //Else it must equal clientIdentify to have got execution thus far
							id = received.getFrom(); //That is now what they're known to us as
							indentified = true;
							
							if(server){
								//Create a new Server Connection
								connection = new ServerConnection(this, id);
								boolean exists = !Core.instance.connections.registerConnection(connection);
								if(exists){
									indentified = false;
									rawMsg("alreadyConnected");
									rawMsg("close");
									close();
									return;
								}
								rawMsg("authenticated");
								continue;
							}
							else{
								rawMsg("unsupportedOperation");
								rawMsg("close");
								indentified = false;
								close();
								Core.logger.warning("Attempted connection from a non-server user! This version of ServerManager doesn't support such connections!");
								//TODO Create connections for non-servers (Eg. site users, app users)
							}
							continue;
						}
						if(isIdentified() && connection != null){ //Allowed to access all other calls now! :)
							Core.instance.eventManager.callEvent(new MessageEvent(connection, received)); //Tell everybody it's been received
						}
					}
				}
			} catch (SocketException e) {
				//No error, it probably just closed
				close();
				return;
			} catch (Exception e) {
				Core.logger.warning("Error in some received data; ignoring it!");
				//Error in data received, just continue to next data
				e.printStackTrace();
			}
		}
		open = false;
		return;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public synchronized void sendRawMsg(Collection<? extends String> msg){
		if(useSendQueue){ //Queue msgs
			outboundQueue.addAll(msg);
			return;
		}
		//Just do it
		for(String m:msg){
			rawMsg(m);
		}
	}
	
	public synchronized void sendRawMsg(String msg){
		if(useSendQueue){ //Queue msgs
			outboundQueue.add(msg);
			return;
		}
		//Just do it
		rawMsg(msg);
	}
	
	private void rawMsg(String msg){
		if(!isOpen()){
			return;
		}
		out.println(msg);
		out.flush();
	}
	
	public void sendKeepAliveMessage(){
		rawMsg("alive");
	}
	
	private synchronized Message processMsg(String in){
		for(TransitMessage msg:inbound){
			if(msg.onRecieve(in)){
				if(msg.hasRecievedAll()){
					return msg.getMessage();
				}
				return null; //It matches and has been recieved
			}
		}
		//Didn't match one already being parsed, so create new
		TransitMessage msg = new TransitMessage(in);
		msg.onRecieve(in); //Make sure it knows
		
		if(msg.hasRecievedAll()){ //Message was only one line, we're done
			return msg.getMessage();
		}
		
		//Message is more than one line, add to the list of being parsed
		inbound.add(msg);
		return null;
	}
	
}
