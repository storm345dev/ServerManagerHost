package org.stormdev.authlib.json.response;

/**
 * The possible errors for when clientDualAuth fails
 *
 */
public enum ClientAuthError {
	/**
	 * The client's session is invalid, maybe they aren't even logged in?
	 */
	INVALIDSESSION;
}
