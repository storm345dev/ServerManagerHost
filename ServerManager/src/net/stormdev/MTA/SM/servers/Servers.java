package net.stormdev.MTA.SM.servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.stormdev.MTA.SM.connections.ServerConnection;

public class Servers {
	private volatile Map<String, Server> servers = new HashMap<String, Server>();
	
	public synchronized int getConnectedCount(){
		return servers.size();
	}
	
	public synchronized void registerServer(ServerConnection con){
		servers.put(con.getConnectionID(), Server.createBlank(con));
	}
	
	public synchronized void registerServer(Server server){
		servers.put(server.getConnection().getConnectionID(), server);
	}
	
	public synchronized Server getServer(String conId){
		return servers.get(conId);
	}
	
	public synchronized boolean serverExists(String conId){
		return servers.containsKey(conId);
	}
	
	public synchronized void disconnectServer(String conId){
		servers.remove(conId);
	}
	
	public synchronized List<Server> getConnectedServers(){
		return new ArrayList<Server>(servers.values());
	}

}
