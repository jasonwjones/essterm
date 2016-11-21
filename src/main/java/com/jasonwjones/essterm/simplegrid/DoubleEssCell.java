package com.jasonwjones.essterm.simplegrid;

import com.jasonwjones.essterm.grid.EssCell;

public class DoubleEssCell implements EssCell {

	private Double value;
	
	public DoubleEssCell(Double value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value.toString(); 
	}
	
	@Override
	public Double getDouble() {
		return value;
	}

	@Override
	public boolean isMissing() {
		return value == null;
	}
	
	@Override
	public EssCellType getCellType() {
		return EssCellType.DATA;
	}

}
