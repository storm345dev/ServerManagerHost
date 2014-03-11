package net.stormdev.MTA.SM.messaging;

import net.stormdev.MTA.SM.connections.Message;
import net.stormdev.MTA.SM.events.Event;

public class MessageEvent implements Event {
	private Message msg;
	public MessageEvent(Message message){
		this.msg = message;
	}
	public Message getMessage(){
		return this.msg;
	}
}
