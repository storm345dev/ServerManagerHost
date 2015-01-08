package net.stormdev.MTA.SM.utils;

import java.util.regex.Pattern;

import org.stormdev.authlib.client.OfflineException;
import org.stormdev.authlib.client.StormAuthLib;
import org.stormdev.authlib.json.response.ServerAuthResponse;
import org.stormdev.authlib.ranks.Rank;

public class StormUser {
	public static StormUser fromString(String in){ //login.getEmail()+"|"+ak
		String[] parts = in.split(Pattern.quote("|"));
		if(parts.length != 2){
			return null;
		}
		
		return new StormUser(parts[0], parts[1]);
	}
	
	public String getEmail() {
		return email;
	}

	public String getFullName() {
		return fullName;
	}

	public String getNickName() {
		return nickName;
	}
	
	public String getProfileURL() {
		return profileURL;
	}

	public boolean isReceiveEmails() {
		return receiveEmails;
	}

	public Rank getStormDevRank() {
		return rank;
	}

	private String email;
	private String fullName;
	private String nickName;
	private String profileURL;
	private boolean receiveEmails;
	private Rank rank;
	private String authKey;
	
	private StormUser(String email, String authKey){
		this.email = email;
		this.authKey = authKey;
	}
	
	public boolean authenticate(){
		ServerAuthResponse sar = null;
		try {
			sar = StormAuthLib.sendServerAuth(email, authKey);
		} catch (OfflineException e) {
			return false;
		}
		if(!sar.wasSuccessful()){
			return false;
		}
		
		this.fullName = sar.getFullName();
		this.nickName = sar.getNickName();
		this.profileURL = sar.getProfilePicURL();
		this.receiveEmails = sar.isSentEmails();
		this.rank = sar.getRank();
		return true;
	}
}
