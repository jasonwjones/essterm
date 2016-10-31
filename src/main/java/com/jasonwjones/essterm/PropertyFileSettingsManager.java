package com.jasonwjones.essterm;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonwjones.essterm.grid.AdhocOptions;

public class PropertyFileSettingsManager implements SettingsManager {

	private static final Logger logger = LoggerFactory.getLogger(PropertyFileSettingsManager.class);
	
	private Set<String> recentlyUsedServers;
	
	private String recentUsername;
	
	private String recentPassword; 
	
	private File settingsFile;
	
	private AdhocOptions adhocOptions;
	
	public PropertyFileSettingsManager(File settingsFile) {
		try {
			this.settingsFile = settingsFile;
			initSettings();
			loadSettings(settingsFile);
			 adhocOptions = new AdhocOptions();
		} catch (IOException e) {
			logger.warn("No settings file found at {}, using defaults", settingsFile);
		}
	}
	
	public void initSettings() {
		recentlyUsedServers = new TreeSet<>();
	}
	
	private final void loadSettings(File file) throws IOException {		
		Properties properties = new Properties();
		try (Reader reader = new FileReader(file)) {	
			properties.load(reader);
		}

		properties.forEach((key, value) -> {
			if (key.toString().startsWith("essterm.connections.recent")) {
				recentlyUsedServers.add(value.toString());
			}
		});
		recentUsername = properties.getProperty("essterm.connections.username");
		recentPassword = properties.getProperty("essterm.connections.password");

		logger.info("Loaded settings from {}", file);
	}
	
	public void saveSettings() throws IOException {
		saveSettings(this.settingsFile);
	}
	
	public void saveSettings(File file) throws IOException {
		Properties properties = new Properties();
		
		int index = 0;
		for (String recentlyUsedServer : recentlyUsedServers) {
			properties.put("essterm.connections.recent." + index++, recentlyUsedServer);
		}
		properties.put("essterm.connections.username", recentUsername);
		properties.put("essterm.connections.password", recentPassword);
		
		try (Writer writer = new FileWriter(file)) {
			properties.store(writer, "Properties updated at " + new Date());
		}
	}
	
	@Override
	public void addRecentlyUsedServer(String server) {
		recentlyUsedServers.add(server);
	}

	@Override
	public Set<String> getRecentlyUsedServers() {
		return recentlyUsedServers;
	}
	
	@Override
	public String getRecentUsername() {
		return recentUsername;
	}

	@Override
	public void setRecentUsername(String recentUsername) {
		this.recentUsername = recentUsername;
	}

	@Override
	public String getRecentPassword() {
		return recentPassword;
	}

	@Override
	public void setRecentPassword(String recentPassword) {
		this.recentPassword = recentPassword;
	}

	@Override
	public String getRecentServer() {
		if (!recentlyUsedServers.isEmpty()) {
			return recentlyUsedServers.iterator().next();
		} else {
			return null;
		}
	}

	@Override
	public AdhocOptions getAdhocOptions() {
		return adhocOptions;
	}

}
