package org.stormdev.authlib.json.response;

import org.stormdev.authlib.api.StormLogin;
import org.stormdev.authlib.ranks.Rank;

/**
 * The response from the server for retrieving login info
 *
 */
public class LoginInfoResponse {
	private boolean success = false;
	private String error = null;
	private String sessionID = null;
	private String email = null;
	private String fullName = null;
	private String nickName = null;
	private String profileURL = null;
	private boolean receiveEmails = false;
	private String rank = null;
	
	/**
	 * If the request was successful, aka they logged in
	 * @return If the login info retrieving was successful
	 */
	public boolean wasSuccessful(){
		return this.success;
	}
	
	/**
	 * If something was wrong with the login (Eg. the login code was wrong, etc...)
	 * @return If the login info retrieving was unsuccessful
	 */
	public boolean wasAnError(){
		return error != null;
	}
	
	/**
	 * Get why the login info retrieving was unsuccessful (Eg. the login code was wrong, etc...)
	 * @return Why the login info retrieving was unsuccessful
	 */
	public LoginInfoError getError(){
		if(!wasAnError()){
			return null;
		}
		return LoginInfoError.valueOf(error.toUpperCase());
	}
	
	/**
	 * Get the sessionID for this StormDev login session, used for logging out, etc...
	 * @return The SessionID
	 */
	public String getSessionID(){
		return this.sessionID;
	}
	
	/**
	 * Get the account's email address
	 * @return The account's email address
	 */
	public String getEmail(){
		return this.email;
	}
	
	/**
	 * Get the user's full name as on StormDev.org
	 * @return The user's full name
	 */
	public String getFullName(){
		return this.fullName;
	}
	
	/**
	 * Get the user's one word nickname as on StormDev.org
	 * @return The user's nick name
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
