package net.stormdev.MTA.SM.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
	private volatile Map<Event, List<Listener<?>>> handlers = new HashMap<Event, List<Listener<?>>>();
	
	public EventManager(){
		
	}
	
	public synchronized <T extends Event> void registerListener(T event, Listener<T> listener){
		List<Listener<?>> handles = new ArrayList<Listener<?>>();
		if(handlers.containsKey(event)){
			handles = handlers.get(event);
		}
		handles.add(listener);
		handlers.put(event, handles);
	}
	
	public <T extends Event> T callEvent(T event){
		List<Listener<?>> handles = new ArrayList<Listener<?>>();
		if(handlers.containsKey(event)){
			handles = handlers.get(event);
		}
		
		for(Listener<?> l:handles){
			try {
				@SuppressWarnings("unchecked")
				Listener<T> list = (Listener<T>)l;
				list.onCall(event);
			} catch (Exception e) {
				//Wrong listener in list
				continue;
			}
		}
		return event;
	}
}
