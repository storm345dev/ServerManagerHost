package org.stormdev.authlib.json.requests;

public class LoginInfoRequest {
	public static String request = "loginInfo";
	private String email;
	private String code;
	
	public LoginInfoRequest(String email, String code){
		this.email = email;
		this.code = code;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
