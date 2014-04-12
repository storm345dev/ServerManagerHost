package net.stormdev.MTA.SM.test;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

import net.stormdev.MTA.SM.messaging.Encrypter;
import net.stormdev.MTA.SM.utils.ListStore;
import net.stormdev.MTA.SM.utils.Scheduler;

public class TestManager {
	private ListStore log;
	public static TestLogger logger;
	public static Random random = new Random();
	
	private int count;
	
	public TestManager(String[] args) throws Exception {
		
		log = new ListStore(new File("Test"+File.separator+Calendar.getInstance().getTimeInMillis()+".txt"));
		logger = new TestLogger(log);
		new Scheduler(); //Initialize it
		
		String arg0 = "10000";
		if(args.length > 0){
			arg0 = args[0];
		}
		count = Integer.parseInt(arg0);
		
		logger.info("Starting test...");
		
		try {
			testEncrypter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.save();
	}
	
	private void testEncrypter(){
		int fails = 0;
		
		logger.info("Testing encrypter...");
		String key = "helloi8mh3r3m4t3";
		Encrypter enc = new Encrypter(key);
		
		for(int i=0;i<count;i++){
			String rand = TestStringGenerator.gen();
			/*
			String key = TestStringGenerator.gen(random.nextInt(11-3)+3);
			while(key.contains("`")){
				key = TestStringGenerator.gen(random.nextInt(11-3)+3);
			}
		    Encrypter enc = new Encrypter(key);
			*/
		
			TestResult result = enc.test(rand);
			//result.printIfFailed();
			if(!result.didPass()){
				fails++;
			}
		}
		
		logger.info("Encrypter test has "+fails+"/"+count+" fails!");
	}
}
