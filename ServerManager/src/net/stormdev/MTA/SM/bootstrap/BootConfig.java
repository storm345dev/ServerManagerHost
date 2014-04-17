package net.stormdev.MTA.SM.bootstrap;

import java.io.File;
import java.io.IOException;

import configuration.InvalidConfigurationException;
import configuration.file.YamlConfiguration;

public class BootConfig {
	private File dataFolder;
	private File bootCfg;
	private YamlConfiguration bootConfig;
	
	public BootConfig(File dataFolder) throws IOException, InvalidConfigurationException{
		this.dataFolder = dataFolder;
		dataFolder.mkdirs();
		bootCfg = new File(dataFolder.getAbsolutePath()+File.separator+"bootConfig.yml");
		if(!bootCfg.exists() || bootCfg.length() < 1){
			bootCfg.createNewFile();
			bootConfig = new YamlConfiguration();
		}
		else{
			bootConfig = new YamlConfiguration();
			bootConfig.load(bootCfg);
		}
		setDefaultsAndLoad();
		bootConfig.save(bootCfg);
	}
	
	public File getFolder(){
		return dataFolder;
	}
	
	public void save() throws IOException{
		bootConfig.save(bootCfg);
	}
	
	private void setDefaultsAndLoad(){
		ConfigSettings[] settings = ConfigSettings.values();
		for(ConfigSettings c:settings){
			String key = c.getConfigKey();
			if(!bootConfig.contains(key)){
				bootConfig.set(key, c.getDefault());
			}
			c.setVal(bootConfig.get(key));
		}
	}
}
