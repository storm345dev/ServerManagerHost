package net.stormdev.MTA.SM.bootstrap;

import java.io.File;
import java.io.IOException;

import configuration.file.YamlConfiguration;

public class BootConfig {
	private File dataFolder;
	private File bootCfg;
	private YamlConfiguration bootConfig;
	
	public BootConfig(File dataFolder) throws IOException{
		this.dataFolder = dataFolder;
		dataFolder.mkdirs();
		bootCfg = new File(dataFolder.getAbsolutePath()+File.separator+"bootConfig.yml");
		if(!bootCfg.exists() || bootCfg.length() < 1){
			bootCfg.createNewFile();
		}
		
	}
}
