package com.jasonwjones.essterm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jasonwjones.essterm.model.ChosenConnection;

@Component
public class ConnectionManager {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	private ChosenConnection currentConnection;
	
	public ChosenConnection getCurrentConnection() {
		return currentConnection;
	}
	
	public void setCurrentConnection(ChosenConnection connection) {
		// kind of a kludge since you can use Esc on conn dialog
		if (connection.getCube() != null) {
			this.currentConnection = connection;
		}
		
		logger.info("The current connection is now {}", connection);
	}
	
	public boolean hasConnection() {
		return currentConnection != null;
	}
	
}
