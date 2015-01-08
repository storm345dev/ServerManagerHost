package org.stormdev.authlib.json.response;

/**
 * All possible errors for logging into StormDev accounts
 *
 */
public enum LoginRequestError {
	/**
	 * There have been too many login attempts within a short space of time, so you have to wait the time specified in milliseconds before trying to login again.
	 */
	COOLDOWN, 
	/**
	 * The email or password for the account was wrong
	 */
	WRONGPASSWORD,
	/**
	 * An unknown error occured when trying to login
	 */
	ERROR;
}
