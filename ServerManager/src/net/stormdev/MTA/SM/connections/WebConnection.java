package net.stormdev.MTA.SM.connections;

import java.util.List;

import net.stormdev.MTA.SM.messaging.MessageRecipient;
import net.stormdev.MTA.SM.utils.GoogleUser;

public class WebConnection implements Connection {
	
	private String conId;
	private ConnectionInterpreter connection;
	private GoogleUser user;
	
	public WebConnection(ConnectionInterpreter interpreter, String webId){
		this.conId = webId;
		this.connection = interpreter;
	}
	
	public GoogleUser getUser(){
		return user;
	}
	
	public boolean hasAuthUser(){
		return user != null;
	}
	
	public void askForAuthUser(){
		sendMsg(new Message(conId, MessageRecipient.HOST.getConnectionID(), "accountRequest", "accountRequest"));
	}
	
	protected boolean loadAuthUser(String in){
		user = GoogleUser.fromString(in);
		
		boolean valid = (user != null) && user.authenticate(); //TODO Also check if they can access this network
		return valid;
	}

	public String getConnectionID() {
		return conId;
	}

	public void sendMsg(String title, String msg, String from) {
		sendMsg(new Message(conId, from, title, msg));
	}

	public void sendMsg(Message message) {
		List<String> toSend = message.getRaw();
		connection.sendRawMsg(toSend);
	}

	public void rawMsg(String msg) {
		connection.sendRawMsg(msg);
	}

	public boolean isConnected() {
		return connection.isOpen()&&connection.checkIfAlive();
	}

	public void disconnect() {
		rawMsg("close");
		connection.close();
	}

}
