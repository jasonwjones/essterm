package com.jasonwjones.essterm.simplegrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.grid.EssCell;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.Point;
import com.jasonwjones.griddly.Grid;
import com.jasonwjones.griddly.impl.BasicGrid;

class SimpleEssGrid implements EssGrid {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEssGrid.class);
	
	private Grid<EssCell> grid;
	
	private int row = 0;
	
	public SimpleEssGrid(int rows, int columns) {
		this.grid = new BasicGrid<EssCell>(rows, columns);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				grid.setCellData(row, col, new SimpleEssCell(String.format("%d, %d", row, col)));
			}
		}
	}
	
	@Override
	public void retrieve() {
		logger.info("Retrieving");
	}

	@Override
	public void zoomIn(Point point) {
		logger.info("Zooming in on {}", point);
		//grid = new DefaultGridShaper<EssCell>(grid).removeTopRows(2);
		logger.info("Shaped");
		logger.info("Change row is {}", row);
		grid.setCellData(row++, 0, new SimpleEssCell("foo"));
	}

	@Override
	public void zoomOut(Point point) {
		logger.info("Zooming out on {}", point);
	}

	@Override
	public void keepOnly(Point point) {
		logger.info("Keeping only on {}", point);
	}

	@Override
	public void removeOnly(Point point) {
		logger.info("Remove only on {}", point);
	}

	@Override
	public void pivot(Point start, Point end) {
		logger.info("Pivot from {} to {}", start, end);
	}
	
	@Override
	public Grid<EssCell> getGrid() {
		return grid;
	}

	@Override
	public void updateCubeViewProperties(AdhocOptions adhocOptions) throws Exception {
		throw new RuntimeException("Not supported");
	}

}
