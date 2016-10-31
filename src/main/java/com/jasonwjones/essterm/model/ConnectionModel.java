package com.jasonwjones.essterm.model;

import java.util.List;

public class ConnectionModel {

	private List<String> recentServers;
	
	private String username;
	
	private String password;
	
	private boolean rememberPassword;
	
	private String application;
	
	private String cube;

	public List<String> getRecentServers() {
		return recentServers;
	}

	public void setRecentServers(List<String> recentServers) {
		this.recentServers = recentServers;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberPassword() {
		return rememberPassword;
	}

	public void setRememberPassword(boolean rememberPassword) {
		this.rememberPassword = rememberPassword;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getCube() {
		return cube;
	}

	public void setCube(String cube) {
		this.cube = cube;
	}
	
}
