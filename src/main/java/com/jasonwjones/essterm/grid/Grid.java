package com.jasonwjones.essterm.grid;

/**
 * A simple 2D grid of cell data, indexed by row and column. Previously essterm depended on the
 * (unrelated, dormant) griddly library for this; the only pieces ever used were this interface's
 * four methods, so they're defined directly here instead.
 */
public interface Grid<E> {

	int getRows();

	int getColumns();

	E getCellData(int row, int col);

	void setCellData(int row, int col, E cellData);
}
