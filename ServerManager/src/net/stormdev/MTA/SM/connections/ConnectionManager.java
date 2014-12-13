package net.stormdev.MTA.SM.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.messaging.MessageRecipient;
import net.stormdev.MTA.SM.servers.Servers;

public class ConnectionManager {
	private volatile Map<String, Connection> connections = new HashMap<String, Connection>();
	private Servers servers;
	
	public ConnectionManager(){
		this.servers = new Servers();
	}
	
	public Servers getServers(){
		return servers;
	}
	
	public synchronized void closeAll(){
		for(Connection con:new ArrayList<Connection>(connections.values())){
			con.disconnect();
		}
	}
	
	public synchronized boolean registerConnection(Connection con){ //true if successful, false if it already exists
		String connectionId = con.getConnectionID();
		if(connections.containsKey(connectionId)){
			return false; //Sorry already exists :(
		}
		connections.put(connectionId, con);
		if(con instanceof ServerConnection){
			servers.registerServer((ServerConnection)con);
			con.sendMsg(new Message(con.getConnectionID(), MessageRecipient.HOST.getConnectionID(), "requestCommand", "serverUpdate"));
		}
		Core.logger.info("Connected: "+con.getConnectionID());
		return true;
	}
	
	public synchronized void unregisterConnection(String connectionId){
		connections.remove(connectionId);
		servers.disconnectServer(connectionId); //If they were a server, disconnect them!
		Core.logger.info("Disconnected: "+connectionId);
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
	
	public synchronized List<Connection> getWebConnections(){
		List<Connection> cons = new ArrayList<Connection>();
		for(Connection c:connections.values()){
			if(c instanceof WebConnection){
				cons.add(c);
			}
		}
		return cons;
	}
	
	public List<Connection> getWebConnectionsNonBlock(){
		List<Connection> cons = new ArrayList<Connection>();
		for(Connection c:new ArrayList<Connection>(connections.values())){
			if(c instanceof WebConnection){
				cons.add(c);
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
