package org.stormdev.authlib.json.response;

import org.stormdev.authlib.ranks.Rank;

/**
 * The response from the server from serverDualAuth, if successful contains the account and session details except the SessionID
 *
 */
public class ServerAuthResponse {
	private boolean success = false;
	private String error = null;
	private String email = null;
	private String fullName = null;
	private String nickName = null;
	private String profileURL = null;
	private boolean receiveEmails = false;
	private String rank = null;
	
	/**
	 * If the serverDualAuth was successful, eg. the client is logged into a valid StormDev account
	 * @return If the serverDualAuth was successful
	 */
	public boolean wasSuccessful(){
		return this.success;
	}
	
	/**
	 * If the serverDualAuth was unsuccessful, eg. they aren't logged in, the AuthKey is wrong, etc...
	 * @return If the serverDualAuth was unsuccessful
	 */
	public boolean wasError(){
		return this.error != null;
	}
	
	/**
	 * Get why the serverDualAuth was successful, eg. they aren't logged in, the AuthKey is wrong, etc...
	 * @return Why the serverDualAuth was unsuccessful
	 */
	public ServerAuthError getError(){
		if(this.error == null){
			return null;
		}
		return ServerAuthError.valueOf(error.toUpperCase());
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
}
