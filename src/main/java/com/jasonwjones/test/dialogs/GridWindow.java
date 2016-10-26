package com.jasonwjones.test.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.jasonwjones.essterm.grid.Point;
import com.jasonwjones.test.EchoingGridWindowDataSource;
import com.jasonwjones.test.EssTable;
import com.jasonwjones.test.EssTable.KeyStrokeDelegate;
import com.jasonwjones.test.dialogs.GridWindow.GridWindowDataSource;

public class GridWindow extends BasicWindow implements KeyStrokeDelegate {

	private static final Logger logger = LoggerFactory.getLogger(GridWindow.class);
	
	private Panel panel;
	
	private EssTable<String> grid;
	
	private GridWindowDataSource dataSource;
	
	private Map<KeyStroke, GridAction> keyBindings;
	
	public GridWindow(String title) {
		super(title);
		setCloseWindowWithEscape(true);
		panel = new Panel();
		
		grid = new EssTable<String>("A", "B", "C");
		grid.setKeyStrokeDelegate(this);
		
		panel.addComponent(grid);
		
		dataSource = new EchoingGridWindowDataSource(10, 10);
		refreshGrid();
		
		keyBindings = new HashMap<>();
		keyBindings.put(new KeyStroke('a', false, false), new ZoomIn());
		keyBindings.put(new KeyStroke('s', false, false), new GridAction() {
			@Override
			public void execute(Point point, GridWindowDataSource dataSource) {
				System.out.println("Zooming out on " + point);				
			}
		});
		
		setComponent(panel);
	}
	
	public void refreshGrid() {
		panel.removeComponent(grid);
		System.out.println("Ref grid");
		String[] labels = new String[dataSource.getCols()];
		for (int i = 1; i <= dataSource.getCols(); i++) {
			labels[i - 1] = "Column " + i;
		}
		grid = new EssTable<String>(labels);
		grid.setKeyStrokeDelegate(this);
		TableModel<String> model = new TableModel<String>(labels);
		
		for (int row = 0; row < dataSource.getRows(); row++) {
			List<String> rowVals = new ArrayList<String>();
			for (int col = 0; col < dataSource.getCols(); col++) {
				rowVals.add(dataSource.getCell(row, col));
			}
			model.addRow(rowVals);
		}
		
		grid.setTableModel(model);
		panel.addComponent(grid);
	}
	
	@Override
	public boolean handleKeyStroke(KeyStroke keyStroke) {
		if (keyStroke.getKeyType().equals(KeyType.Character)) {
			Point point = Point.of(grid.getSelectedRow(), grid.getSelectedColumn());
			System.out.println("Processing char " + keyStroke.getCharacter() + " at " + point);
			
			GridAction gridAction = keyBindings.get(keyStroke);
			if (gridAction != null) {
				gridAction.execute(point, dataSource);
			}
			return true;
		}
		return false;
	}
	
	public interface GridAction {
		
		public void execute(Point point, GridWindowDataSource dataSource);
		
	}
	
	public static class ZoomIn implements GridAction {
		public void execute(Point point, GridWindowDataSource dataSource) {
			System.out.println("Zooming in on " + point);
		}
	}
	
	
	public interface GridWindowDataSource {
		
		public int getRows();
		
		public int getCols();
		
		public String getCell(int row, int col);
		
		public void zoomIn(Point point);
		
	}
	
}
