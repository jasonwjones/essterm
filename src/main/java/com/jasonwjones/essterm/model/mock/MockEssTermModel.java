package com.jasonwjones.essterm.model.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jasonwjones.essterm.model.EssTermModel;

@Component
public class MockEssTermModel implements EssTermModel {

	private List<String> applications;
	
	public MockEssTermModel() {
		this.applications = Arrays.asList("Sample", "ASOSamp");
	}

	@Override
	public List<String> getApplications(String server, String username, String password) {
		return applications;
	}

	@Override
	public List<String> getCubes(String server, String username, String password, String application) {
		List<String> cubes = new ArrayList<>();
		for (int index = 0; index < 3; index++) {
			cubes.add(application + " " + index);
		}
		return cubes;
	}
		
}
