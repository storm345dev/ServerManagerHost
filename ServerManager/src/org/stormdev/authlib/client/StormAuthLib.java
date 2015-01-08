package org.stormdev.authlib.client;

import org.stormdev.authlib.api.StormLogin;
import org.stormdev.authlib.json.GsonUtil;
import org.stormdev.authlib.json.requests.ClientAuthRequest;
import org.stormdev.authlib.json.requests.DoActivityRequest;
import org.stormdev.authlib.json.requests.EmailAvailableRequest;
import org.stormdev.authlib.json.requests.LoginInfoRequest;
import org.stormdev.authlib.json.requests.LoginRequest;
import org.stormdev.authlib.json.requests.LogoutByPasswordRequest;
import org.stormdev.authlib.json.requests.LogoutBySessionIDRequest;
import org.stormdev.authlib.json.requests.ServerAuthRequest;
import org.stormdev.authlib.json.requests.SessionValidateRequest;
import org.stormdev.authlib.json.response.ClientAuthResponse;
import org.stormdev.authlib.json.response.LoginInfoResponse;
import org.stormdev.authlib.json.response.LoginRequestResponse;
import org.stormdev.authlib.json.response.ServerAuthResponse;

/**
 * Class to manage all auth requests to StormDev.
 *
 */
public class StormAuthLib {
	/**
	 * The URL of the StormDev AuthServlet root, can be changed using "StormAuthLib.AUTH_URL = ...".
	 */
	public static String AUTH_URL = "http://stormdev.org/auth/";
	
	/**
	 * Used to test the client to make sure it's working. 
	 * The method does a variety of requests to StormDev and prints if the request was successful and if the response was as expected.
	 * @param args args[0] = email, args[1] = password
	 */
	public static void main(String args[]){
		String EMAIL = "name@example.com";
		String PASS = "password";
		
		if(args.length > 1){
			EMAIL = args[0];
			PASS = args[1];
		}
		
		try {
			LoginRequestResponse loginResp = login(EMAIL, PASS);
			if(!loginResp.wasSuccessful()){
				System.out.println("Login failed: "+loginResp.getError());
				return;
			}
			System.out.println("Successfully logged in!");
			
			String sessionID = loginResp.getSessionID();
			
			boolean validSession = isSessionValid(EMAIL, sessionID);
			System.out.println("Valid session: "+validSession);
			System.out.println("Activity done: "+doActivity(EMAIL, sessionID));
			
			ClientAuthResponse car = sendClientAuth(EMAIL, sessionID);
			System.out.println("ClientAuth successfully sent: "+car.wasSuccessful());
			
			ServerAuthResponse sar = sendServerAuth(EMAIL, car.getAuthKey());
			System.out.println("ServerAuth successful: "+sar.wasSuccessful());
			
			System.out.println("Successfully logged out: "+logoutBySessionID(EMAIL, sessionID));
		} catch (OfflineException e) {
			// ;(
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Used to create a new login session or retrieve an existing one with StormDev using the account email and password.
	 * 
	 * @param email The account email
	 * @param password The account password
	 * @return The response from the server, as a java object for convenience
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static LoginRequestResponse login(String email, String password) throws OfflineException{
		LoginRequest lr = new LoginRequest(email, password);
		String reply = HttpClient.executeJSONPost(AUTH_URL+LoginRequest.request, lr);
		if(reply == null){
			throw new OfflineException();
		}
		return GsonUtil.gson.fromJson(reply, LoginRequestResponse.class);
	}
	
	/**
	 * Used to retrieve the login information after a user has logged in using third party authentication. More information on 
	 * third party authentication is available on stormdev.org.
	 * 
	 * @param email The account email (Sent to your page as a URL Parameter)
	 * @param code The one time use, unique code from StormDev given when the user logged in (Sent to your page as a URL Parameter)
	 * @return The response from the server, as a java object for convenience
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static LoginInfoResponse getLoginInfo(String email, String code) throws OfflineException{
		LoginInfoRequest lir = new LoginInfoRequest(email, code);
		String reply = HttpClient.executeJSONPost(AUTH_URL+LoginInfoRequest.request, lir);
		if(reply == null){
			throw new OfflineException();
		}
		return GsonUtil.gson.fromJson(reply, LoginInfoResponse.class);
	}
	
	/**
	 * Used to check if a StormDev account exists for the given email.
	 * 
	 * @param email The email to check if in use
	 * @return True=The email is in use as a StormDev account, False=The email isn't in use as a StormDev account
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean isEmailInUse(String email) throws OfflineException{
		EmailAvailableRequest ear = new EmailAvailableRequest(email);
		String reply = HttpClient.executeJSONPost(AUTH_URL+EmailAvailableRequest.request, ear);
		if(reply == null){
			throw new OfflineException();
		}
		return !Boolean.parseBoolean(reply.trim());
	}
	
	/**
	 * Used to determine if a StormDev login session is valid and that the user hasn't logged out
	 * 
	 * @param login A StormLogin object containing the data from the login. Can be obtained by LoginRequestResponse.toStormLogin() or LoginInfoResponse.toStormLogin()
	 * @return True=The session is valid, False=The session is invalid (They may have logged out)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean isSessionValid(StormLogin login) throws OfflineException{
		return isSessionValid(login.getEmail(), login.getSessionID());
	}
	
	/**
	 * Used to determine if a StormDev login session is valid and that the user hasn't logged out
	 * 
	 * @param email The account email
	 * @param sessionID The SessionID of the StormDev login session
	 * @return True=The session is valid, False=The session is invalid (They may have logged out)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean isSessionValid(String email, String sessionID) throws OfflineException{
		SessionValidateRequest svr = new SessionValidateRequest(email, sessionID);
		String reply = HttpClient.executeJSONPost(AUTH_URL+SessionValidateRequest.request, svr);
		if(reply == null){
			throw new OfflineException();
		}
		return Boolean.parseBoolean(reply.trim());
	}
	
	/**
	 * Tell StormDev the login session is active (aka the user is doing things) so that the account isn't logged out automatically for being idle.
	 * 
	 * @param login A StormLogin object containing the data from the login. Can be obtained by LoginRequestResponse.toStormLogin() or LoginInfoResponse.toStormLogin()
	 * @return True=Success, False=Error (Are they still logged in?)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean doActivity(StormLogin login) throws OfflineException{
		return doActivity(login.getEmail(), login.getSessionID());
	}
	
	/**
	 * Tell StormDev the login session is active (aka the user is doing things) so that the account isn't logged out automatically for being idle.
	 * 
	 * @param email The account email
	 * @param sessionID The SessionID of the StormDev login session
	 * @return True=Success, False=Error (Are they still logged in?)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean doActivity(String email, String sessionID) throws OfflineException{
		DoActivityRequest dar = new DoActivityRequest(email, sessionID);
		String reply = HttpClient.executeJSONPost(AUTH_URL+DoActivityRequest.request, dar);
		if(reply == null){
			throw new OfflineException();
		}
		return Boolean.parseBoolean(reply.trim());
	}
	
	/**
	 * Logout a StormDev login session (Invalidate it)
	 * 
	 * @param login A StormLogin object containing the data from the login. Can be obtained by LoginRequestResponse.toStormLogin() or LoginInfoResponse.toStormLogin()
	 * @return True=Success, False=Error (They already logged out on StormDev.org perhaps?)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean logout(StormLogin login) throws OfflineException{
		return logoutBySessionID(login.getEmail(), login.getSessionID());
	}
	
	/**
	 * Logout a StormDev login session (Invalidate it)
	 * 
	 * @param email The account email
	 * @param sessionID The SessionID of the StormDev login session
	 * @return True=Success, False=Error (They already logged out on StormDev.org perhaps?)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean logoutBySessionID(String email, String sessionID) throws OfflineException{
		LogoutBySessionIDRequest lbsir = new LogoutBySessionIDRequest(email, sessionID);
		String reply = HttpClient.executeJSONPost(AUTH_URL+LogoutBySessionIDRequest.request, lbsir);
		if(reply == null){
			throw new OfflineException();
		}
		return Boolean.parseBoolean(reply.trim());
	}
	
	/**
	 * Logout a StormDev login session (Invalidate it)
	 * 
	 * @param email The account email
	 * @param password The account password
	 * @return True=Success, False=Error (They already logged out on StormDev.org perhaps?)
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static boolean logoutBySessionPassword(String email, String password) throws OfflineException{
		LogoutByPasswordRequest lbpr = new LogoutByPasswordRequest(email, password);
		String reply = HttpClient.executeJSONPost(AUTH_URL+LogoutByPasswordRequest.request, lbpr);
		if(reply == null){
			throw new OfflineException();
		}
		return Boolean.parseBoolean(reply.trim());
	}
	
	/**
	 * This is used when a client wants to prove it's genuinely logged into a StormDev login session to a server or other client, but doesn't want to send the sessionID (Because that'd give the other client/server control of the session). Eg. When a game wants to join a server. The client will first make a request to clientDualAuth which will return an AuthKey. Next then there is 1 minute for the other client/server to send a request to serverDualAuth with the AuthKey and if it's valid will be told the same info as with login except without the sessionID.
	 * 
	 * @param login A StormLogin object containing the data from the login. Can be obtained by LoginRequestResponse.toStormLogin() or LoginInfoResponse.toStormLogin()
	 * @return The response from the server (Including AuthKey), as a java object for convenience
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static ClientAuthResponse sendClientAuth(StormLogin login) throws OfflineException{
		return sendClientAuth(login.getEmail(), login.getSessionID());
	}
	
	/**
	 * This is used when a client wants to prove it's genuinely logged into a StormDev login session to a server or other client, but doesn't want to send the sessionID (Because that'd give the other client/server control of the session). Eg. When a game wants to join a server. The client will first make a request to clientDualAuth which will return an AuthKey. Next then there is 1 minute for the other client/server to send a request to serverDualAuth with the AuthKey and if it's valid will be told the same info as with login except without the sessionID.
	 * 
	 * @param email The account email
	 * @param sessionID The SessionID of the StormDev login session
	 * @return The response from the server (Including AuthKey), as a java object for convenience
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static ClientAuthResponse sendClientAuth(String email, String sessionID) throws OfflineException{
		ClientAuthRequest car = new ClientAuthRequest(email, sessionID);
		String reply = HttpClient.executeJSONPost(AUTH_URL+ClientAuthRequest.request, car);
		if(reply == null){
			throw new OfflineException();
		}
		return GsonUtil.gson.fromJson(reply, ClientAuthResponse.class);
	}
	
	/**
	 * This is the second part of a client trying to prove it's logged in to a valid StormDev login session to a server or other client. In this part, the AuthKey sent to the client in the first stage has been sent to the server/other client and now that server/other client needs to make a request to serverDualAuth to validate it and retrieve the login information.
	 * 
	 * @param email The account email
	 * @param clientAuthKey The AuthKey from sendClientAuth's response
	 * @return The response from the server (Including all the account and session information except the sessionID), as a java object for convenience
	 * @throws OfflineException Thrown when a request to StormDev fails. (Either StormDev or you are offline)
	 */
	public static ServerAuthResponse sendServerAuth(String email, String clientAuthKey) throws OfflineException{
		ServerAuthRequest sar = new ServerAuthRequest(email, clientAuthKey);
		String reply = HttpClient.executeJSONPost(AUTH_URL+ServerAuthRequest.request, sar);
		if(reply == null){
			throw new OfflineException();
		}
		return GsonUtil.gson.fromJson(reply, ServerAuthResponse.class);
	}
}
