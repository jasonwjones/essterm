package com.jasonwjones.essterm.grid;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.griddly.Grid;

public interface EssGrid {
	
	public enum ZoomOptions {
		INCLUDE_SELECTION
	}
	
	public ChosenConnection getConnection();
	
	public void retrieve();

	/**
	 * Discards the current view and replaces it with a fresh default retrieve, as if a brand new
	 * grid had just been opened. Useful for recovering from a zoom/pivot sequence that's left the
	 * grid in a confusing shape.
	 */
	public void resetToDefault();
	
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

	/**
	 * The names of the dimensions on this grid's cube, for member selection.
	 */
	public List<String> getDimensionNames();

	/**
	 * The root of a dimension's member hierarchy (the dimension itself), for lazily browsing its
	 * members in a member selection dialog.
	 *
	 * @param dimensionName the dimension to browse
	 */
	public EssMemberNode getDimensionRoot(String dimensionName);

	/**
	 * Places the given members into the grid, replacing whichever member currently occupies each
	 * target position.
	 *
	 * @param placements the members to place, and where
	 */
	public void setMembers(List<MemberPlacement> placements);

}
