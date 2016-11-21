package com.jasonwjones.essterm.grid;

import com.jasonwjones.essterm.model.ChosenConnection;

public interface EssGridFactory {

	public EssGrid createEssGrid(ChosenConnection connection);
	
	public MemberInfoResolver getMemberInfoResolver(); 
	
}
