package net.stormdev.MTA.SM.test;

public class TestMain {
	public static void main(String[] args){
		try {
			new TestManager(args);
		} catch (Exception e) {
			// We should end the program
			return;
		}
		return;
	}
}
