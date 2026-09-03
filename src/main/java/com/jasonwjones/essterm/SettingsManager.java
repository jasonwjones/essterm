package com.jasonwjones.essterm;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.essterm.model.ChosenConnection.Backend;

public interface SettingsManager {

	public void addRecentlyUsedServer(Backend backend, String server);

	public Set<String> getRecentlyUsedServers(Backend backend);

	public String getRecentServer(Backend backend);

	/**
	 * Records a fully-chosen connection (server, credentials, application, cube) as the most recent
	 * one, for the "Recents" launcher menu. If an equivalent connection (same backend, server,
	 * username, application, and cube) is already recorded, it's moved to the front and its password
	 * updated, rather than duplicated.
	 */
	public void addRecentConnection(ChosenConnection connection);

	/**
	 * Fully-chosen connections, most recently used first, for the "Recents" launcher menu.
	 */
	public List<ChosenConnection> getRecentConnections();

	public String getRecentUsername();
	
	public void setRecentUsername(String recentUsername);
	
	public String getRecentPassword();
	
	public void setRecentPassword(String password);
	
	public void saveSettings() throws IOException;

	public AdhocOptions getAdhocOptions();
	
}
