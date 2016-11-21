package com.jasonwjones.essterm.grid;

import java.util.Collection;
import java.util.EnumSet;

import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.griddly.Grid;

public interface EssGrid {
	
	public enum ZoomOptions {
		INCLUDE_SELECTION
	}
	
	public ChosenConnection getConnection();
	
	public void retrieve();
	
	/**
	 * Zooms in on the point, using the current zoom settings
	 * 
	 * @param point
	 */
	public void zoomIn(Point point);
	
	public void zoomIn(Point point, EnumSet<ZoomOptions> zoomOptions) throws Exception;

	public void zoomOut(Point point);
			
	public void keepOnly(Point point);
			
	public void removeOnly(Point point);
		
	public void pivot(Point start, Point end);
	
	public void setData(Point point, double value) throws Exception;
	
	public void clearData(Point point) throws Exception;
	
	public Grid<EssCell> getGrid();
		
	public Collection<String> getCalcScripts() throws EssGridException;
	
	public void runCalc(String calcName) throws Exception;
	
	public void updateCubeViewProperties(AdhocOptions adhocOptions) throws Exception;
	
}
