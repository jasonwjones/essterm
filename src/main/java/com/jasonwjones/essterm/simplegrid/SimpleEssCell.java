package com.jasonwjones.essterm.simplegrid;

import com.jasonwjones.essterm.grid.EssCell;

public class SimpleEssCell implements EssCell {

	private String value;
	
	public SimpleEssCell(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}
