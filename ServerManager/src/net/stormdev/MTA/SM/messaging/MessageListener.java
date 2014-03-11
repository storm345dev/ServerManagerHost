package net.stormdev.MTA.SM.messaging;

import java.util.ArrayList;
import java.util.List;

import net.stormdev.MTA.SM.connections.Connection;
import net.stormdev.MTA.SM.connections.Message;
import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.core.Main;
import net.stormdev.MTA.SM.events.Listener;

public class MessageListener implements Listener<MessageEvent> {

	private Main main;
	public MessageListener(){
		main = Core.instance;
		Core.instance.eventManager.registerListener(new MessageEvent(null), this); //Registers the event to us
	}
	
	public void onCall(MessageEvent event) {
		Message message = event.getMessage();
		Core.logger.debug("Recieved Message: "+message.getMsg());
		
		String to = message.getTo();
		if(to.equalsIgnoreCase(MessageRecipient.HOST.getConnectionID())){
			//TODO Manage messages to us
			
			
			
			
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