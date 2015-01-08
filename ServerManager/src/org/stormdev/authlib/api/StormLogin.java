package org.stormdev.authlib.api;

import org.stormdev.authlib.ranks.Rank;

/**
 * A class which contains all the information about a logged in user that is given by StormDev.
 *
 */
public class StormLogin {
	private String email;
	private String sessionID;
	private String fullName;
	private String nickName;
	private String profilePicURL;
	private boolean receiveEmails;
	private Rank rank;
	
	public StormLogin(String email, String sessionID, String fullName, String nickName, String profilePicURL, boolean receiveEmails, Rank rank){
		this.email = email;
		this.sessionID = sessionID;
		this.fullName = fullName;
		this.nickName = nickName;
		this.profilePicURL = profilePicURL;
		this.receiveEmails = receiveEmails;
		this.rank = rank;
	}

	/**
	 * The email associated with this StormDev account.
	 * @return Their email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * The sessionID associated with the StormDev account's login session. This can be used to logout the user and make authenticated requests to StormDev.
	 * @return The SessionID for this login session
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * The user's full name as entered on StormDev.org.
	 * @return Their full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * The user's one word nickname as entered on StormDev.org.
	 * @return Their nick name
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * A URL which points to the user's profile picture as shown on StormDev.org, this can be put into a HTML <img> tag, etc...
	 * @return The URL pointing to the user's profile picture
	 */
	public String getProfilePicURL() {
		return profilePicURL;
	}

	/**
	 * If the user wants to receive emails about promotions, news, etc... from StormDev. 
	 * @return True = They want to receive emails, False = They don't
	 */
	public boolean isReceiveEmails() {
		return receiveEmails;
	}

	/**
	 * Their rank on the StormDev website
	 * @return The rank, Eg. DEFAULT, ADMIN, SUPER_ADMIN, OWNER
	 */
	public Rank getRank() {
		return rank;
	}
}
