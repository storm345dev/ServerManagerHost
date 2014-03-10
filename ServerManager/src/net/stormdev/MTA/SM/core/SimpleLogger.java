package net.stormdev.MTA.SM.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

public class SimpleLogger implements Logger {
	
	private boolean debug;
	public SimpleLogger(boolean debug){
		this.debug = debug;
	}
	
	private String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	private void out(String msg){
		System.out.println("["+getTime()+"] " + msg);
	}
	
	private void out(Level level, String msg){
		out("["+level.getName().toUpperCase()+"] "+msg);
	}

	public void log(String msg, Level level) {
		out(level, msg);
	}

	public void log(String msg) {
		out(msg);
	}

	public void debug(String msg, Level level) {
		if(!debug){
			return;
		}
		log(msg, level);
	}

	public void debug(String msg) {
		if(!debug){
			return;
		}
		log(msg);
	}

	public void info(String msg) {
		out("<INFO> "+msg);
	}

	public void info(String msg, Level level) {
		out(level, "<INFO> "+msg);
	}

	public void error(String msg) {
		out("<ERROR> "+msg);
	}

	public void error(String msg, Level level) {
		out(level, "<ERROR> "+msg);	
	}

	public void error(String msg, Level level, Exception error) {
		error(msg, level);
		error.printStackTrace();
	}

	public void error(String msg, Exception error) {
		error(msg);
		error.printStackTrace();
	}

	public void error(Level level, Exception error) {
		out(level, error.getLocalizedMessage());
		error.printStackTrace();
	}

	public void warning(String msg) {
		out("<WARN> "+msg);
	}

	public void warning(String msg, Level level) {
		out(level, "<WARN> "+msg);
	}

}
