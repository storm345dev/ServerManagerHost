package net.stormdev.MTA.SM.connections;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TransitMessage {
	
	private String id;
	private volatile List<String> parts = new ArrayList<String>();
	int toRecieve;
	
	public TransitMessage(String firstRecieved){
		parts.add(firstRecieved);
		
		String[] segs = firstRecieved.split(Pattern.quote("|"));
		
		id = segs[6]; //The ID segment
		toRecieve = Integer.parseInt(segs[4]); //The number to recieve
	}
	
	public Message getMessage(){
		if(!hasRecievedAll()){
			throw new RuntimeException("Bad access to message in transit! Attempted message read before all recieved!");
		}
		return Message.fromRaw(parts);
	}
	
	public boolean hasRecievedAll(){
		return parts.size()>=toRecieve;
	}
	
	public boolean onRecieve(String msg){ //Return true if it's out message that is matched by the ID
		String rId = msg.split(Pattern.quote("|"))[6]; //The ID segment;
		if(!rId.equals(id)){
			return false; //Not us
		}
		
		parts.add(msg);
		
		return true;
	}
}
