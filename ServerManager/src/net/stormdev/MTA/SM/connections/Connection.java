package net.stormdev.MTA.SM.connections;

public interface Connection {
	public String getServerID();
	public void sendMsg(String title, String msg, String from);
	public void sendMsg(Message message);
	public void rawMsg(String msg);
	public boolean isConnected();
	public void disconnect();
}
