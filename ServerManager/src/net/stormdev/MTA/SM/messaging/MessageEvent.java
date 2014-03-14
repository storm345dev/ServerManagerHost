package net.stormdev.MTA.SM.messaging;

import net.stormdev.MTA.SM.connections.Connection;
import net.stormdev.MTA.SM.connections.Message;
import net.stormdev.MTA.SM.events.Event;

public class MessageEvent implements Event {
	private Message msg;
	private Connection sender;
	public MessageEvent(Connection sender, Message message){
		this.msg = message;
		this.sender = sender;
	}
	public Connection getSender(){
		return sender;
	}
	public Message getMessage(){
		return this.msg;
	}
}
