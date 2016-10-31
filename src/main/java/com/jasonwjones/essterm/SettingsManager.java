package com.jasonwjones.essterm;

import java.io.IOException;
import java.util.Set;

import com.jasonwjones.essterm.grid.AdhocOptions;

public interface SettingsManager {

	public void addRecentlyUsedServer(String server);
	
	public Set<String> getRecentlyUsedServers();
	
	public String getRecentServer();
	
	public String getRecentUsername();
	
	public void setRecentUsername(String recentUsername);
	
	public String getRecentPassword();
	
	public void setRecentPassword(String password);
	
	public void saveSettings() throws IOException;

	public AdhocOptions getAdhocOptions();
	
}
