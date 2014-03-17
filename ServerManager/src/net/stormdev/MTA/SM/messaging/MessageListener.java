package net.stormdev.MTA.SM.messaging;

import java.util.ArrayList;
import java.util.List;

import net.stormdev.MTA.SM.connections.Connection;
import net.stormdev.MTA.SM.connections.Message;
import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.events.Listener;
import net.stormdev.MTA.SM.servers.Server;
import net.stormdev.MTA.SM.utils.Scheduler;

public class MessageListener implements Listener<MessageEvent> {

	private Main main;
	public MessageListener(){
		main = Core.instance;
		Core.instance.eventManager.registerListener(new MessageEvent(null, null), this); //Registers the event to us
	}
	
	public void onCall(final MessageEvent event) {
		Message message = event.getMessage();
		String to = message.getTo();
		final String from = message.getFrom();
		String title = message.getMsgTitle();
		
		if(to.equalsIgnoreCase(MessageRecipient.HOST.getConnectionID())){
			Core.logger.debug("Recieved Message: "+message.getMsg());
			//TODO Manage messages to us
			if(title.equals("serverUpdate")){
				String msg = message.getMsg();
				Server server = main.connections.getServers().getServer(from);
				if(server != null){
					try {
						server.handleUpdatePacket(msg);
					} catch (Exception e) {
						// Invalid packet!
						Core.logger.error("Invalid server update message format! Disconnecting them!");
						event.getSender().disconnect();
					}
				}
				return;
			}
			else if(title.equals("getServers")){
				final List<Server> servers = main.connections.getServers().getConnectedServers();
				Scheduler.instance.runTaskAsync(new Runnable(){

					public void run() { //Don't want it pausing the receiving thread (Where the events are called from)
						StringBuilder toSend = new StringBuilder();
						for(Server s:servers){
							if(toSend.length() < 1){
								toSend.append(s.getRaw());
								continue;
							}
							toSend.append(","+s.getRaw());
						}
						Message msg = new Message(from, MessageRecipient.HOST.getConnectionID(), "servers", toSend.toString());
						event.getSender().sendMsg(msg); //Reply
						return;
					}});
			}
			
			
			
			return;
		}
		
		//It's not for us... so forward it on
		List<Connection> sendTo = new ArrayList<Connection>();
		if(to.equalsIgnoreCase(MessageRecipient.ALL.getConnectionID())){
			//Send it to everybody
			sendTo.addAll(main.connections.getServerConnections());
		}
		else {
			Connection con = main.connections.getConnection(to);
			if(con == null){
				//Invalid
				return;
			}
			sendTo.add(con); //Just send it to who it asked for
		}
		
		for(Connection c:sendTo){
			c.sendMsg(message); //Successfully forwarded!
		}
		
		return;
	}
	
}
