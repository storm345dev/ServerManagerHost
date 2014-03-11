package net.stormdev.MTA.SM.connections;

import java.util.List;


public class ServerConnection implements Connection {
	private ConnectionInterpreter manager;
	private String id;
	public ServerConnection(ConnectionInterpreter interpreter, String serverId){
		//TODO Register ourselves
		this.id = serverId;
		this.manager = interpreter;
	}

	public String getServerID() {
		return id;
	}

	public void sendMsg(String title, String msg, String from) {
		Message message = new Message(id, from, title, msg);
		sendMsg(message);
	}

	public void sendMsg(Message message) {
		List<String> toSend = message.getRaw();
		manager.sendRawMsg(toSend);
	}

	public void rawMsg(String msg) {
		manager.sendRawMsg(msg);
	}

	public boolean isConnected() {
		return manager.isOpen()&&manager.isOpen()&&manager.checkIfAlive();
	}

	public void disconnect() {
		rawMsg("close");
		manager.close();
	}
}
