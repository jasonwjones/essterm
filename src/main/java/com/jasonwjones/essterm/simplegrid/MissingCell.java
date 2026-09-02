package com.jasonwjones.essterm.simplegrid;

import com.jasonwjones.essterm.grid.EssCell;

public class MissingCell implements EssCell {

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public Double getDouble() {
		return null;
	}

	@Override
	public boolean isMissing() {
		return true;
	}

	@Override
	public EssCellType getCellType() {
		return EssCellType.DATA;
	}

}
