package com.jasonwjones.test;

@Deprecated
public class EchoingGridWindowDataSource  {

	private int rows;
	
	private int cols;
	
	public EchoingGridWindowDataSource(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	public String getCell(int row, int col) {
		return String.format("Cell R=%d, C=%d", row, col);
	}
	
	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

//	@Override
//	public void zoomIn(Point point) {
//		// TODO Auto-generated method stub
//		
//	}

}
