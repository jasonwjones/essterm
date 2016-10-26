package com.jasonwjones.essterm.grid;

public class Point {

	private int row;

	private int col;

	private Point(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public static Point of(int row, int col) {
		return new Point(row, col);
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return "Point [row=" + row + ", col=" + col + "]";
	}
	
}
