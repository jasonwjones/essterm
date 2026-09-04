package com.jasonwjones.essterm.grid;

import java.util.ArrayList;
import java.util.List;

/** A fixed-size {@link Grid} backed by a list of lists. */
public class BasicGrid<E> implements Grid<E> {

	private final int rows;
	private final int columns;
	private final List<List<E>> cells;

	public BasicGrid(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.cells = new ArrayList<>(rows);
		for (int row = 0; row < rows; row++) {
			List<E> rowCells = new ArrayList<>(columns);
			for (int col = 0; col < columns; col++) {
				rowCells.add(null);
			}
			cells.add(rowCells);
		}
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getColumns() {
		return columns;
	}

	@Override
	public E getCellData(int row, int col) {
		return cells.get(row).get(col);
	}

	@Override
	public void setCellData(int row, int col, E cellData) {
		cells.get(row).set(col, cellData);
	}
}
