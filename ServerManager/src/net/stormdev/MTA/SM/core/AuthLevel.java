package net.stormdev.MTA.SM.core;

public enum AuthLevel {
	OWNER(5), DEVELOPER(4), OPERATOR(3), ADMIN(2), USER(1);

	private int access = 0;
	private AuthLevel(int accessNum){
		this.access = accessNum;
	}

	private int getAccessNum(){
		return access;
	}

	public boolean canUse(AuthLevel min){
		int low = min.getAccessNum();
		return access >= low;
	}
}
