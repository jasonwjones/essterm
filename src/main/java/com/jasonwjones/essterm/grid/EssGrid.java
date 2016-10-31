package com.jasonwjones.essterm.grid;

import com.jasonwjones.griddly.Grid;

public interface EssGrid {
	
	public void retrieve();
	
	/**
	 * Zooms in on the point, using the current zoom settings
	 * 
	 * @param point
	 */
	public void zoomIn(Point point);

	public void zoomOut(Point point);
			
	public void keepOnly(Point point);
			
	public void removeOnly(Point point);
		
	public void pivot(Point start, Point end);
	
	public Grid<EssCell> getGrid();
		
	public void updateCubeViewProperties(AdhocOptions adhocOptions) throws Exception;
	
}
