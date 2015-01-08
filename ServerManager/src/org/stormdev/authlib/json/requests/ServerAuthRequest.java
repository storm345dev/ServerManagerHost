package org.stormdev.authlib.json.requests;

public class ServerAuthRequest {
	public static String request = "serverDualAuth";
	private String email;
	private String authKey;
	
	public ServerAuthRequest(String email, String authKey){
		this.email = email;
		this.authKey = authKey;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	
	
}
