package org.stormdev.authlib.ranks;

/**
 * Represents a rank on StormDev.org
 *
 */
public enum Rank {
	/**
	 * A normal user
	 */
	DEFAULT(0), 
	/**
	 * An administrator that can do various things such as post on StormDev.org
	 */
	ADMIN(1), 
	/**
	 * An administrator that can do most things on StormDev.org, such as change a user's rank, etc...
	 */
	SUPER_ADMIN(2), 
	/**
	 * An owner of StormDev, can do everything on StormDev.org
	 */
	OWNER(3);
	
	private int no;
	private Rank(int no){
		this.no = no;
	}
	
	public boolean canUse(Rank r){
		return r.no <= this.no;
	}
	
	public String getUserFriendlyName(){
		String raw = name().toLowerCase().replaceAll("_", " ");
		return capitalise(raw);
	}
	
	private String capitalise(String text){
		if(text.length() < 2){
			return text.toUpperCase();
		}
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
}
