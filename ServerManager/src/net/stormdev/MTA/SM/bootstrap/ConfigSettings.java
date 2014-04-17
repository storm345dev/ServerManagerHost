package net.stormdev.MTA.SM.bootstrap;

public enum ConfigSettings {
PORT("boot.port", 50000), PASS("boot.pass", "pass");

	private Object def;
	private String key;
	private Object val = null;
	private ConfigSettings(String key, Object def){
		this.key = key;
		this.def = def;
	}
	
	public String getConfigKey(){
		return key;
	}
	
	public Object getDefault(){
		return def;
	}
	
	public Object get(){
		return val;
	}
	
	public void setVal(Object val){
		this.val = val;
	}

}
