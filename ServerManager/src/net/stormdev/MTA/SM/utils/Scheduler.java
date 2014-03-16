package net.stormdev.MTA.SM.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
	public static Scheduler instance;
	
	private volatile ExecutorService executor;
	private volatile List<Runnable> pendingSyncTasks = new ArrayList<Runnable>();
	
	public Scheduler(){
		instance = this;
		executor = Executors.newCachedThreadPool();
	}
	
	public void shutdown(){
		executor.shutdown();
	}
	
	public synchronized void pullQueue(){ //Runs all pending sync tasks, must be done sync
		int limit = 5;
		int count = 0;
		for(Runnable task:new ArrayList<Runnable>(pendingSyncTasks)){
			if(count >= limit){
				return; //Stop executing function
			}
			try {
				task.run();
			}
			finally {
				pendingSyncTasks.remove(task);
				count++;
			}
		}
	}
	
	public List<Runnable> shutdownNow(){
		return executor.shutdownNow();
	}
	
	public void runTaskAsync(Runnable run){
		executor.submit(run);
	}
	
	public synchronized void runTaskSync(Runnable run){
		pendingSyncTasks.add(run);
	}
}
