package org.stormdev.authlib.json.requests;

public class SessionValidateRequest {
	public static String request = "validateSession";
	private String email;
	private String sessionID;
	
	public SessionValidateRequest(String email, String sessionID){
		this.email = email;
		this.sessionID = sessionID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	
}
