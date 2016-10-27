package com.jasonwjones.essterm.model;

import java.util.List;

public interface EssTermModel {

	public List<String> getRecentServers();
	
	public List<String> getApplications(String server, String username, String password);
	
	public List<String> getCubes(String server, String username, String password, String application);
	
}
