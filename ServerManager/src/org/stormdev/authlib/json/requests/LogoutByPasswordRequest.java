package org.stormdev.authlib.json.requests;

public class LogoutByPasswordRequest {
	public static String request = "logout";
	private String email;
	private String password;
	
	public LogoutByPasswordRequest(String email, String password){
		this.email = email;
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
