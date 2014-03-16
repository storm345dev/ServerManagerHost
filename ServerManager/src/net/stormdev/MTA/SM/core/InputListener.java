package net.stormdev.MTA.SM.core;

import net.stormdev.MTA.SM.events.CommandInputEvent;
import net.stormdev.MTA.SM.events.Listener;
import net.stormdev.MTA.SM.utils.Scheduler;

public class InputListener implements Listener<CommandInputEvent> {
	
	private final Main main;
	public InputListener(){
		main = Core.instance;
		Core.instance.eventManager.registerListener(CommandInputEvent.class, this);
	}

	public void onCall(CommandInputEvent event) {
		String in = event.getInput();
		if(in.equalsIgnoreCase("help")){
			Core.logger.info("Commands:");
			Core.logger.info("'help' - Shows this list");
			Core.logger.info("'stop' - Stops the program");
			return;
		}
		else if(in.equalsIgnoreCase("stop") || in.equalsIgnoreCase("end")){
			Scheduler.instance.runTaskSync(new Runnable(){

				public void run() {
					Core.logger.info("Stopping...");
					main.shutdown();
					return;
				}});
			return;
		}
		else{
			Core.logger.info("Unknown command! Do 'help' for help");
		}
		return;
	}

}
