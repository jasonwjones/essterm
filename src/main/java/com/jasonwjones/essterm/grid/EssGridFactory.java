package com.jasonwjones.essterm.grid;

import com.jasonwjones.test.models.ChosenConnection;

public interface EssGridFactory {

	//public EssGrid createEssGrid(String server, String username, String password, String application, String database);
	public EssGrid createEssGrid(ChosenConnection connection);
	
	public MemberInfoResolver getMemberInfoResolver(); 
	
}
