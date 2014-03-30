package net.stormdev.MTA.SM.connections;

import java.util.List;

public class WebConnection implements Connection {
	
	private String conId;
	private ConnectionInterpreter connection;
	
	public WebConnection(ConnectionInterpreter interpreter, String webId){
		this.conId = webId;
		this.connection = interpreter;
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
