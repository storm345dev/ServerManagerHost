package net.stormdev.MTA.SM.messaging;

import net.stormdev.MTA.SM.connections.Message;
import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.events.Listener;

public class MessageListener implements Listener<MessageEvent> {

	public MessageListener(){
		Core.instance.eventManager.registerListener(new MessageEvent(null), this); //Registers the event to us
	}
	
	public void onCall(MessageEvent event) {
		Message message = event.getMessage();
		Core.logger.debug("Recieved Message: "+message.getMsg());
		return;
	}
	
}
