package com.jasonwjones.essterm;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.model.ChosenConnection.Backend;

public class PropertyFileSettingsManager implements SettingsManager {

	private static final Logger logger = LoggerFactory.getLogger(PropertyFileSettingsManager.class);

	/**
	 * Recently-used servers are tracked separately per backend, since a JAPI server and a REST
	 * endpoint URL are never valid interchangeably. Persisted as {@code <BACKEND>|<server>} so old
	 * (pre-REST) plain server entries can still be read back as legacy JAPI entries.
	 */
	private Map<Backend, Set<String>> recentlyUsedServers;

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
		recentlyUsedServers = new EnumMap<>(Backend.class);
		for (Backend backend : Backend.values()) {
			recentlyUsedServers.put(backend, new TreeSet<>());
		}
	}

	private final void loadSettings(File file) throws IOException {
		Properties properties = new Properties();
		try (Reader reader = new FileReader(file)) {
			properties.load(reader);
		}

		properties.forEach((key, value) -> {
			if (key.toString().startsWith("essterm.connections.recent")) {
				String entry = value.toString();
				Backend backend = Backend.JAPI;
				String server = entry;
				int separator = entry.indexOf('|');
				if (separator >= 0) {
					try {
						backend = Backend.valueOf(entry.substring(0, separator));
						server = entry.substring(separator + 1);
					} catch (IllegalArgumentException e) {
						// not a recognized "BACKEND|server" entry - treat the whole value as a
						// legacy (pre-REST) JAPI server, same as if there were no separator at all
					}
				}
				recentlyUsedServers.get(backend).add(server);
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
		for (Backend backend : Backend.values()) {
			for (String recentlyUsedServer : recentlyUsedServers.get(backend)) {
				properties.put("essterm.connections.recent." + index++, backend + "|" + recentlyUsedServer);
			}
		}
		properties.put("essterm.connections.username", recentUsername);
		properties.put("essterm.connections.password", recentPassword);

		try (Writer writer = new FileWriter(file)) {
			properties.store(writer, "Properties updated at " + new Date());
		}
	}

	@Override
	public void addRecentlyUsedServer(Backend backend, String server) {
		recentlyUsedServers.get(backend).add(server);
	}

	@Override
	public Set<String> getRecentlyUsedServers(Backend backend) {
		return recentlyUsedServers.get(backend);
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
	public String getRecentServer(Backend backend) {
		Set<String> servers = recentlyUsedServers.get(backend);
		if (!servers.isEmpty()) {
			return servers.iterator().next();
		} else {
			return null;
		}
	}

	@Override
	public AdhocOptions getAdhocOptions() {
		return adhocOptions;
	}

}
