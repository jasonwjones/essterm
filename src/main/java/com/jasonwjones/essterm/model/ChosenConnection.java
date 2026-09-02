package com.jasonwjones.essterm.model;

public class ChosenConnection {

	public enum Backend {
		JAPI, REST
	}

	private Backend backend = Backend.JAPI;

	private String server;

	private String username;

	private String password;

	private String application;

	private String cube;

	public Backend getBackend() {
		return backend;
	}

	public void setBackend(Backend backend) {
		this.backend = backend;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
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

	@Override
	public String toString() {
		return "ChosenConnection [backend=" + backend + ", server=" + server + ", username=" + username
				+ ", application=" + application + ", cube=" + cube + "]";
	}
	
}
