package net.stormdev.MTA.SM.connections;

public interface Connection {
	public String getServerName();
	public void msg(String msg);
	public void rawMsg(String msg);
	public boolean isConnected();
}
