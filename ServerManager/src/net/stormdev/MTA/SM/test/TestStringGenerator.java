package net.stormdev.MTA.SM.test;

public class TestStringGenerator {
	public static String gen(int length){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<length;i++){
			int rand = TestManager.random.nextInt(126-21)+21;
			while(rand == 96){ // No ` or , allowed
				rand = TestManager.random.nextInt(126-21)+21;
			}
			char[] c = Character.toChars(rand);
			builder.append(c);
		}
		return builder.toString();
	}
	
	public static String gen(){
		int rand = TestManager.random.nextInt(1000-3)+3;
		return gen(rand);
	}
}
