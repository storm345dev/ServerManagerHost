package net.stormdev.MTA.SM.core;

import java.util.logging.Level;


public interface Logger {
	public void log(String msg, Level level);
	public void log(String msg);
	public void debug(String msg, Level level);
	public void debug(String msg);
	public void info(String msg);
	public void info(String msg, Level level);
	public void error(String msg);
	public void error(String msg, Level level);
	public void error(String msg, Level level, Exception error);
	public void error(String msg, Exception error);
	public void error(Level level, Exception error);
	public void warning(String msg);
	public void warning(String msg, Level level);
}
