package net.stormdev.MTA.SM.core;

import java.util.regex.Pattern;

public class AuthAccount {
	private AuthType type;
	private String name;
	private AuthLevel level;
	
	public AuthAccount(String name, AuthType type, AuthLevel level){
		this.name = name;
		this.type = type;
		this.level = level;
	}
	
	public AuthAccount(String in) throws Exception{ //Type:name:perm eg. 'google:storm345dev@gmail.com:OWNER'
		String[] parts = in.trim().split(Pattern.quote(":"));
		if(parts.length < 3){
			throw new Exception("Invalid!");
		}
		String type = parts[0];
		String perm = parts[2];
		
		this.name = parts[1];
		if(type.equalsIgnoreCase("GOOGLE")){
			this.type = AuthType.STORMDEV;
			System.out.println("WARNING: Accounts on MineManager now use StormDev accounts! Email "+name+" is being used as they were defined as a google account! To remove this message, change 'GOOGLE' accounts in the users.txt to 'STORMDEV' accounts");
		}
		else {
			this.type = AuthType.valueOf(type);
		}
		this.level = AuthLevel.valueOf(perm);
		if(type == null || level == null){
			throw new Exception("Invalid!");
		}
	}
	
	public String getName(){
		return name;
	}
	
	public AuthType getAuthType(){
		return type;
	}
	
	public AuthLevel getAuthLevel(){
		return level;
	}
	
	public String asString(){
		return type.name()+":"+name+":"+level.name();
	}
}
