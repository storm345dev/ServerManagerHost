package net.stormdev.MTA.SM.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.stormdev.MTA.SM.utils.ListStore;

public class AccountAuthentication {
	
	private ListStore file;
	
	List<AuthAccount> accounts = new ArrayList<AuthAccount>();
	
	public AccountAuthentication(){
		this.file = new ListStore(new File("Data"+File.separator+"users.txt"));
		load();
	}
	
	public synchronized AuthAccount get(AuthType type, String name){
		for(AuthAccount account:new ArrayList<AuthAccount>(accounts)){
			if(account.getAuthType().equals(type) && account.getName().equals(name)){
				return account;
			}
		}
		return null;
	}
	
	private synchronized void load(){
		accounts.clear();
		file.load();
		List<String> lines = file.getValues();
		if(lines.size() < 1){
			setDefaults();
		}
		for(String line:lines){
			AuthAccount acc;
			try {
				acc = new AuthAccount(line);
			} catch (Exception e) {
				Core.logger.warning("Invalid account declaration at line: "+line);
				continue;
			}
			accounts.add(acc);
		}
	}
	
	public synchronized void add(AuthAccount account){
		accounts.add(account);
		save();
	}
	
	public synchronized void remove(AuthType type, String name){
		for(AuthAccount account:new ArrayList<AuthAccount>(accounts)){
			if(account.getAuthType().equals(type) && account.getName().equals(name)){
				accounts.remove(account);
			}
		}
		save();
	}
	
	private void setDefaults(){
		accounts.clear();
		accounts.add(new AuthAccount("itsjustbjarn@gmail.com", AuthType.GOOGLE, AuthLevel.OWNER));
		accounts.add(new AuthAccount("storm345dev@gmail.com", AuthType.GOOGLE, AuthLevel.DEVELOPER));
		accounts.add(new AuthAccount("mtabuilder@gmail.com", AuthType.GOOGLE, AuthLevel.DEVELOPER));
		accounts.add(new AuthAccount("friend@gmail.com", AuthType.GOOGLE, AuthLevel.OPERATOR));
		accounts.add(new AuthAccount("admin@gmail.com", AuthType.GOOGLE, AuthLevel.ADMIN));
		accounts.add(new AuthAccount("helper@gmail.com", AuthType.GOOGLE, AuthLevel.USER));
		accounts.add(new AuthAccount("563b78a0a7fb4c3b8d2aeba7a2c169f1", AuthType.MINECRAFT, AuthLevel.OWNER));
		accounts.add(new AuthAccount("dc5c98cc1ae242fcb4b4de9e01021f0c", AuthType.MINECRAFT, AuthLevel.OWNER));
		accounts.add(new AuthAccount("someMojangId", AuthType.MINECRAFT, AuthLevel.ADMIN));
		accounts.add(new AuthAccount("someOtherMojangId", AuthType.MINECRAFT, AuthLevel.USER));
		save();
	}
	
	public synchronized void save(){
		ArrayList<String> lines = new ArrayList<String>();
		for(AuthAccount account:accounts){
			lines.add(account.asString());
		}
		file.set(lines);
		file.save();
	}
}
