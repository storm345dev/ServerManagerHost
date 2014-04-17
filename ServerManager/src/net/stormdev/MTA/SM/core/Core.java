package net.stormdev.MTA.SM.core;

import net.stormdev.MTA.SM.utils.Scheduler;

public class Core {
	public static Main instance = null;
	public static Logger logger = null;
	public final static boolean debug = false;
	
	public static void main(String[] args){
		logger = new SimpleLogger(debug);

		logger.info("Starting program...");
		if(instance != null){
			logger.error("ServerManager already running IN THE SAME JVM??? WTF???? WHY???? U BIG NUB NUB LORD. THIS message should be sooo very hard to make appear.");
			return;
		}
		else{
			instance = new Main(args, false);
			instance.begin();
		}
		int amt = Scheduler.instance.shutdownNow().size();
		Core.logger.info(amt+" tasks still running!");
		logger.info("Program terminated!");
		return;
	}
}
