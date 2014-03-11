package net.stormdev.MTA.SM.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManager {
	private volatile Map<String, Connection> connections = new HashMap<String, Connection>();
	
	public ConnectionManager(){
		
	}
	
	public synchronized boolean registerConnection(Connection con){ //true if successful, false if it already exists
		String connectionId = con.getConnectionID();
		if(connections.containsKey(connectionId)){
			return false; //Sorry already exists :(
		}
		connections.put(connectionId, con);
		return true;
	}
	
	public synchronized void unregisterConnection(String connectionId){
		connections.remove(connectionId);
	}
	
	public synchronized Connection getConnection(String connectionId){
		return connections.get(connectionId); //Null if not exists
	}
	
	public synchronized boolean connectionExistsFor(String connectionId){
		return connections.containsKey(connectionId);
	}
	
	public synchronized List<String> getConnectionIds(){
		return new ArrayList<String>(connections.keySet());
	}
	
	public synchronized List<String> getServerConnectionIds(){
		List<String> cons = new ArrayList<String>();
		for(Connection c:connections.values()){
			if(c instanceof ServerConnection){
				cons.add(c.getConnectionID());
			}
		}
		return cons;
	}
	
	public synchronized List<Connection> getConnections(){
		return new ArrayList<Connection>(connections.values());
	}
	
	public synchronized List<Connection> getServerConnections(){
		List<Connection> cons = new ArrayList<Connection>();
		for(Connection c:connections.values()){
			if(c instanceof ServerConnection){
				cons.add(c);
			}
		}
		return cons;
	}
	
}
