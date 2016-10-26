package com.jasonwjones.test.dialogs;

import java.util.Arrays;
import java.util.List;

import com.jasonwjones.test.dialogs.ConnectionDialogWindow.ConnectionDialogWindowDelegate;

public class TestConnectionDialogWindowDelegate implements ConnectionDialogWindowDelegate {

	public void connect(String server, String username, String password) {
		System.out.println("Connect");
	}

	public List<String> getApplications() {
		return Arrays.asList("App 1", "App 2");
	}

	public List<String> getCubes(String application) {
		return Arrays.asList("Cube 1", "Cube 2");
	}

	public void choseCube(String application, String cube) {
		System.out.println("Let's use " + application + " / " + cube);
	}

	@Override
	public List<String> getApplications(String server, String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void choseCube(String server, String username, String password, String application, String cube) {
		// TODO Auto-generated method stub
		
	}

}
