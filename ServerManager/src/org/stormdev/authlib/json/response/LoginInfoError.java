package org.stormdev.authlib.json.response;

/**
 * All the possible errors from a LoginInfo request
 *
 */
public enum LoginInfoError {
	/**
	 * The third party login expired (It's been longer than a minute since the user logged in)
	 */
	EXPIRED, 
	/**
	 * The login code sent to StormDev did not match the one generated
	 */
	INVALIDCODE, 
	/**
	 * The user has logged out since logging in
	 */
	NOTLOGGEDIN;
}
