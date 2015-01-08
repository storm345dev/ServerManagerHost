package org.stormdev.authlib.json.response;

import org.stormdev.authlib.api.StormLogin;
import org.stormdev.authlib.ranks.Rank;

/**
 * The response from the server for trying to login a user
 *
 */
public class LoginRequestResponse {
	private boolean success = false;
	private String error = null;
	private long wait = -1;
	private String sessionID = null;
	private String email = null;
	private String fullName = null;
	private String nickName = null;
	private String profileURL = null;
	private boolean receiveEmails = false;
	private String rank = null;
	
	/**
	 * If the request was successful, aka they logged in
	 * @return If the login was successful
	 */
	public boolean wasSuccessful(){
		return this.success;
	}
	
	/**
	 * If something was wrong with the login (Eg. the password was wrong, etc...)
	 * @return If the login was unsuccessful
	 */
	public boolean wasAnError(){
		return error != null;
	}
	
	/**
	 * Get why the login was unsuccessful (Eg. the password was wrong, etc...)
	 * @return Why the login was unsuccessful
	 */
	public LoginRequestError getError(){
		if(!wasAnError()){
			return null;
		}
		return LoginRequestError.valueOf(error.toUpperCase());
	}
	
	/**
	 * If the user has had too many attempts in a short space of time and has to wait before trying to login again
	 * @return If the user has to wait before trying to login again
	 */
	public boolean isCooldownUntilNextRequest(){
		return this.wait > 0;
	}
	
	/**
	 * Returns how long the user has to wait before trying to login again, in milliseconds
	 * @return In milliseconds, how long the user has to wait before trying to login again
	 */
	public long getRemainingCooldownTimeUntilNextRequestMS(){
		return this.wait;
	}
	
	/**
	 * The sessionID associated with the StormDev account's login session. This can be used to logout the user and make authenticated requests to StormDev.
	 * @return The SessionID for this login session
	 */
	public String getSessionID(){
		return this.sessionID;
	}
	
	/**
	 * The email associated with this StormDev account.
	 * @return Their email
	 */
	public String getEmail(){
		return this.email;
	}
	
	/**
	 * The user's full name as entered on StormDev.org.
	 * @return Their full name
	 */
	public String getFullName(){
		return this.fullName;
	}
	
	/**
	 * The user's one word nickname as entered on StormDev.org.
	 * @return Their nick name
	 */
	public String getNickName(){
		return this.nickName;
	}
	
	/**
	 * A URL which points to the user's profile picture as shown on StormDev.org, this can be put into a HTML <img> tag, etc...
	 * @return The URL pointing to the user's profile picture
	 */
	public String getProfilePicURL(){
		return this.profileURL;
	}
	
	/**
	 * If the user wants to receive emails about promotions, news, etc... from StormDev. 
	 * @return True = They want to receive emails, False = They don't
	 */
	public boolean isSentEmails(){
		return this.receiveEmails;
	}
	
	/**
	 * Their rank on the StormDev website
	 * @return The rank, Eg. DEFAULT, ADMIN, SUPER_ADMIN, OWNER
	 */
	public Rank getRank(){
		if(rank == null){
			return Rank.DEFAULT;
		}
		return Rank.valueOf(rank.toUpperCase());
	}
	
	/**
	 * Generate a StormLogin object for this session, useful to use with StormAuthLib to easily logout the user later or check if they're still logged in
	 * @return The StormDev login session as a StormLogin object
	 */
	public StormLogin toStormLogin(){
		if(!wasSuccessful()){
			return null;
		}
		return new StormLogin(getEmail(), getSessionID(), getFullName(), getNickName(), getProfilePicURL(), isSentEmails(), getRank());
	}
}
