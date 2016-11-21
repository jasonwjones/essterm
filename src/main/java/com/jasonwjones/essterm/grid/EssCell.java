package com.jasonwjones.essterm.grid;

public interface EssCell {

	public enum EssCellType {
		
		MEMBER, DATA, OTHER
		
	}
	
	public String getValue();
	
	public Double getDouble();
	
	public boolean isMissing();
	
	public EssCellType getCellType();
	
}
