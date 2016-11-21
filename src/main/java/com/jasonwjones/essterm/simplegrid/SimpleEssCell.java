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
	
	@Override
	public Double getDouble() {
		return Double.NaN;
	}

	@Override
	public boolean isMissing() {
		return value == null;
	}
	
	@Override
	public EssCellType getCellType() {
		return EssCellType.MEMBER;
	}
	
}
