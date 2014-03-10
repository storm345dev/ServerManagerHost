package net.stormdev.MTA.SM.events;

public interface Listener<T extends Event> {
	public void onCall(T event);
}
