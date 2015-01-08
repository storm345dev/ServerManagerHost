package org.stormdev.authlib.json.requests;

public class EmailAvailableRequest {
	public static String request = "checkAvailability";
	private String email;
	
	public EmailAvailableRequest(String email){
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
}
