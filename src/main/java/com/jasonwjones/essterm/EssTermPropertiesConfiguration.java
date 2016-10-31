package com.jasonwjones.essterm;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonwjones.essterm.model.ConnectionModel;

public class EssTermPropertiesConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(EssTermPropertiesConfiguration.class);
		
	public EssTermPropertiesConfiguration(String filename) {
		logger.info("Loading properties from {}", filename);
	}
	
	public void save(ConnectionModel connections) {
		
	}
	
	public ConnectionModel getConnectionModel() {
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setRecentServers(Arrays.asList("epm11124", "epm11123"));
		connectionModel.setUsername("admin");
		connectionModel.setPassword("password");
		return connectionModel;
	}	
	
}
