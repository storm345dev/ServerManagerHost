package net.stormdev.MTA.SM.utils;

import java.util.regex.Pattern;

import net.stormdev.MTA.SM.http.SimpleHttpClient;

public class GoogleUser {
	
	private static final DeployType deploy = DeployType.ONLINE;
	private static final String authURL; //TODO To be changed when launched...
	static {
		switch(deploy){
		case LOCAL: authURL = "http://localhost:8080/MineManager/auth";
			break;
		case LOCAL_ROOT: authURL = "localhost:8080/auth";
			break;
		case ONLINE: authURL = "http://minemanager.org/auth";
			break;
		default: authURL = "http://minemanager.org/auth";
			break;
		}
	}
	
	private String email;
	private String name;
	private String id;
	private String dispName;
	private String profilePicURL;
	private String locale;
	private String uuid;
	
	private GoogleUser(String email, String name, String id, String dispName, String profilePicURL, String locale, String uuid){
		this.email = email;
	    this.name = name;
	    this.id = id;
	    this.dispName = dispName;
	    this.profilePicURL = profilePicURL;
	    this.locale = locale;
	    this.uuid = uuid;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getFullName(){
		return name;
	}
	
	public String getID(){
		return id;
	}
	
	public String getNickName(){
		return dispName;
	}
	
	public String getProfilePicURL(){
		return profilePicURL;
	}
	
	public String getLocale(){
		return locale;
	}
	
	public String asString(){
		return name+"|"+email+"|"+id+"|"+dispName+"|"+profilePicURL+"|"+locale+"|"+uuid+"|";
	}
	
	public boolean authenticate(){
		String response = SimpleHttpClient.post(authURL, new String[]{"accEmail", "accUUID"}, new String[]{email, uuid});
		if(response == null || response.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}
	
	public static GoogleUser fromString(String in){
		String[] parts = in.split(Pattern.quote("|"));
		if(parts.length < 7){
			return null;
		}
		String name = parts[0];
		String email = parts[1];
		String id = parts[2];
		String dispName = parts[3];
		String profilePicURL = parts[4];
		String locale = parts[5];
		String uuid = parts[6];
		GoogleUser user = new GoogleUser(email, name, id, dispName, profilePicURL, locale, uuid);
		return user;
	}
}