package com.jasonwjones.essterm.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.jasonwjones.essterm.EssTable;
import com.jasonwjones.essterm.EssTable.KeyStrokeDelegate;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.Point;

public class AdhocGridWindow extends BasicWindow implements KeyStrokeDelegate {

	private static final Logger logger = LoggerFactory.getLogger(AdhocGridWindow.class);
	
	private Panel panel;
	
	private EssTable<String> grid;
	
	private EssGrid gridData;
	
	private Map<KeyStroke, GridAction> keyBindings;
	
	private boolean inPivot = false;
	
	private Point pivotStart;
	
	public AdhocGridWindow(String title, EssGrid gridData) {
		super(title);
		this.setHints(Arrays.asList(Hint.EXPANDED));
		this.gridData = gridData;
		setCloseWindowWithEscape(true);
		panel = new Panel();
				
		grid = new EssTable<String>("Dummy");
		grid.setKeyStrokeDelegate(this);
		
		grid.setVisibleColumns(4);
		
		panel.addComponent(new Label("Hi there"));
		panel.addComponent(grid);
		
		refreshGrid();
		
		keyBindings = new HashMap<>();
		keyBindings.put(new KeyStroke('a', false, false), new ZoomIn());
		
		keyBindings.put(new KeyStroke('s', false, false), new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				//System.out.println("Zooming out on " + point);		
				dataSource.zoomOut(point);
			}
		});

		keyBindings.put(new KeyStroke('q', false, false), new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {		
				dataSource.keepOnly(point);
			}
		});

		keyBindings.put(new KeyStroke('w', false, false), new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {		
				dataSource.removeOnly(point);
			}
		});
		
		keyBindings.put(new KeyStroke('v', false, false), new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				if (!inPivot) {
					inPivot = true;
					pivotStart = point;
					logger.info("Pivot anchor is at {}", point);
				} else {
					dataSource.pivot(pivotStart, point);
					inPivot = false;	
				}
			}
		});
		
		keyBindings.put(new KeyStroke('p', false, false), new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				buildCellDialog().showDialog(getTextGUI());
			}
		});
		
		keyBindings.put(new KeyStroke(',', false, false), new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				buildCellDialog().showDialog(getTextGUI());
			}
		});
		
		setComponent(panel);
	}
	
	public void refreshGrid() {
		//if 
		//panel.removeComponent(grid);
		//System.out.println("Ref grid");
		String[] labels = new String[gridData.getGrid().getColumns()];
		for (int i = 1; i <= gridData.getGrid().getColumns(); i++) {
			labels[i - 1] = "Column " + i;
		}
		//grid = new EssTable<String>(labels);
		//grid.setKeyStrokeDelegate(this);
		TableModel<String> model = new TableModel<String>(labels);
		grid.setTableModel(model);
		
		for (int row = 0; row < gridData.getGrid().getRows(); row++) {
			List<String> rowVals = new ArrayList<String>();
			for (int col = 0; col < gridData.getGrid().getColumns(); col++) {
				rowVals.add(gridData.getGrid().getCellData(row, col).getValue());
			}
			model.addRow(rowVals);
		}
		
		logger.info("IS invalid: {}", grid.isInvalid());
		//grid.setTableModel(model);
		//grid.invalidate();
		logger.info("IS invalid: {}", grid.isInvalid());
		//panel.addComponent(grid);
		logger.info("Refreshed");
	}
	
	@Override
	public boolean handleKeyStroke(KeyStroke keyStroke) {
		if (keyStroke.getKeyType().equals(KeyType.Character)) {
			Point point = Point.of(grid.getSelectedRow(), grid.getSelectedColumn());
			GridAction gridAction = keyBindings.get(keyStroke);
			if (gridAction != null) {
				gridAction.execute(point, gridData);
				refreshGrid();
			}
			return true;			
		}
		return false;
	}
	
	public interface GridAction {
		
		public void execute(Point point, EssGrid dataSource);
		
	}
	
	public static class ZoomIn implements GridAction {
		public void execute(Point point, EssGrid dataSource) {
			//System.out.println("Zooming in on " + point);
			dataSource.zoomIn(point);
		}
	}

	public static ActionListDialog buildMemberCellDialog() {
		ActionListDialog actions = new ActionListDialogBuilder()
				.addAction("Zoom In", new Runnable() {
					@Override
					public void run() {
						logger.info("Zoom in on the cell");
						// TODO Auto-generated method stub
						
					}})
				.addAction("Member Selection", new Runnable() {
					@Override
					public void run() {
						logger.info("Perform member selection");						
					}
				})
				.setCanCancel(true)
				.setTitle("Member Cell Actions")
				.setDescription("Choose an action to perform on the selected member")
				.setListBoxSize(new TerminalSize(20, 4))
				.build();
		return actions;	
	}
	
	public static ActionListDialog buildCellDialog() {
		ActionListDialog actions = new ActionListDialogBuilder()
				.addAction("Enter data", new Runnable() {

					@Override
					public void run() {
						logger.info("Wants to update data");
						// TODO Auto-generated method stub
						
					}})
				.addAction("Clear data", new Runnable() {

					@Override
					public void run() {
						logger.info("Clear the data in the cell");						
					}
					
				})
				.setCanCancel(true)
				.setTitle("Data Cell Actions")
				.setDescription("Choose an action to perform on the selected data cell")
				.setListBoxSize(new TerminalSize(20, 4))
				.build();
		return actions;
	}
}
