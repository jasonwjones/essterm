package com.jasonwjones.essterm;

import java.io.IOException;
import java.util.Set;

import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.model.ChosenConnection.Backend;

public interface SettingsManager {

	public void addRecentlyUsedServer(Backend backend, String server);

	public Set<String> getRecentlyUsedServers(Backend backend);

	public String getRecentServer(Backend backend);

	public String getRecentUsername();
	
	public void setRecentUsername(String recentUsername);
	
	public String getRecentPassword();
	
	public void setRecentPassword(String password);
	
	public void saveSettings() throws IOException;

	public AdhocOptions getAdhocOptions();
	
}
