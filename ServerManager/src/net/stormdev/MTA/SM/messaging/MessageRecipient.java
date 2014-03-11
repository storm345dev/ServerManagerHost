package net.stormdev.MTA.SM.messaging;

public class MessageRecipient {
	
	public static final MessageRecipient SELF = create("SELF");
	public static final MessageRecipient ALL = create("ALL");
	
	public static MessageRecipient create(String connectionId){ //Use a factory method in case we need to check or register stuff later
		return new MessageRecipient(connectionId);
	}

	private String connectionId;
	private MessageRecipient(String connectionId){
		this.connectionId = connectionId;
	}
	
	public String getConnectionID(){
		return connectionId;
	}
}
