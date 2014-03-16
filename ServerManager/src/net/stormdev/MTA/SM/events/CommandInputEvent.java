package net.stormdev.MTA.SM.events;

public class CommandInputEvent implements Event {
	private String input;
	public CommandInputEvent(String input){
		this.input = input;
	}
	public String getInput(){
		return input;
	}
}
