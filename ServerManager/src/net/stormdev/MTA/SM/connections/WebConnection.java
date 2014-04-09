package net.stormdev.MTA.SM.connections;

import java.util.List;

import net.stormdev.MTA.SM.core.AuthAccount;
import net.stormdev.MTA.SM.core.AuthType;
import net.stormdev.MTA.SM.core.Core;
import net.stormdev.MTA.SM.messaging.MessageRecipient;
import net.stormdev.MTA.SM.utils.GoogleUser;

public class WebConnection implements Connection {
	
	private String conId;
	private ConnectionInterpreter connection;
	private GoogleUser user;
	private AuthAccount auth;
	
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
		if(user == null){
			System.out.println("Refused: User null!");
		}
		boolean valid = (user != null) && user.authenticate();
		if(!valid){
			System.out.println("Refused: Bad MM login!");
		}
		auth = !valid ? null:Core.instance.accAuths.get(AuthType.GOOGLE, user.getEmail());
		valid = !valid ? false:auth!=null; //If auth==null, set valid to false
		if(!valid){
			System.out.println("Refused: No perms!");
		}
		return valid;
	}
	
	public AuthAccount getAuth(){
		return auth;
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
