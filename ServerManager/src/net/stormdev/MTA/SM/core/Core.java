package net.stormdev.MTA.SM.core;

public class Core {
	public static Main instance = null;
	public static Logger logger = null;
	public final static boolean debug = true;
	
	public static void main(String[] args){
		logger = new SimpleLogger(debug);

		logger.info("Starting program...");
		if(instance != null){
			logger.error("ServerManager already running IN THE SAME JVM??? WTF???? WHY???? U BIG NUB NUB LORD. THIS message should be sooo very hard to make appear.");
			return;
		}
		else{
			instance = new Main(args);
			instance.begin();
		}
		return;
	}
}
