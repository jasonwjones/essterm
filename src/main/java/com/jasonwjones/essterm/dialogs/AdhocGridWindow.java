package com.jasonwjones.essterm.dialogs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListener;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.jasonwjones.essterm.EssTable;
import com.jasonwjones.essterm.EssTable.KeyStrokeDelegate;
import com.jasonwjones.essterm.dialogs.adhoc.KeyBindingManager;
import com.jasonwjones.essterm.dialogs.adhocoptions.AdhocOptionsDialogWindow;
import com.jasonwjones.essterm.grid.AdhocOptions;
import com.jasonwjones.essterm.grid.EssCell;
import com.jasonwjones.essterm.grid.EssCell.EssCellType;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGrid.ZoomOptions;
import com.jasonwjones.essterm.grid.Point;
import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.essterm.model.EssGridMemberSelectionWindowModel;

/**
 * This interface element manages a grid, handles keyboard input, and provides
 * additional UI controls related to the ad hoc experience.
 * 
 * @author jasonwjones
 *
 */
public class AdhocGridWindow extends BasicWindow implements KeyStrokeDelegate {

	private static final Logger logger = LoggerFactory.getLogger(AdhocGridWindow.class);

	private Panel panel;

	private EssTable<String> grid;

	private EssGrid gridData;

	private Map<KeyStroke, AdhocGridAction> keyActionBinding;
	private Map<AdhocGridAction, GridAction> actionGridActionBinding;

	private boolean inPivot = false;

	private Point pivotStart;

	private DecimalFormat formatter = new DecimalFormat("#,###.00");

	private AdhocOptions options;

	private int visibleColumns = 8;

	private static final String DEFAULT_STATUS = "Press ? for key bindings";

	private Label statusLabel;

	private boolean showingAllBindings = true;

	public AdhocGridWindow(EssGrid gridData, AdhocOptions options) {
		super();
		this.setHints(Arrays.asList(Hint.EXPANDED));
		this.gridData = gridData;
		this.options = options;
		ChosenConnection conn = gridData.getConnection();
		setTitle(String.format("Ad hoc grid: %s.%s", conn.getApplication(), conn.getCube()));
		setCloseWindowWithEscape(true);
		panel = new Panel(new BorderLayout());

		grid = new EssTable<String>("Dummy");
		grid.setLayoutData(BorderLayout.Location.CENTER);
		// grid.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		grid.setKeyStrokeDelegate(this);

		grid.setVisibleColumns(visibleColumns);

		// panel.addComponent(new Label("Hi there"));
		panel.addComponent(grid);

		keyActionBinding = KeyBindingManager.defaultKeyBindings();

		Panel statusPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
		statusLabel = new Label(showingAllBindings ? buildAllBindingsText() : DEFAULT_STATUS);
		statusPanel.addComponent(statusLabel);
		statusPanel.setLayoutData(BorderLayout.Location.BOTTOM);
		panel.addComponent(statusPanel);

		refreshGrid();

		actionGridActionBinding = new HashMap<>();
		// keyBindings = new HashMap<>();
		actionGridActionBinding.put(AdhocGridAction.ZOOM_IN, new ZoomIn());
		// keyBindings.put(new KeyStroke('a', false, false), new ZoomIn());

		actionGridActionBinding.put(AdhocGridAction.ZOOM_IN_INCLUDE_SELECTION, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				try {
					dataSource.zoomIn(point, EnumSet.of(ZoomOptions.INCLUDE_SELECTION));
				} catch (Exception e) {
					logger.error("Error zooming");
				}
			}
		});

		actionGridActionBinding.put(AdhocGridAction.KEY_BINDING_OPTIONS, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				new KeyBindingsWindow(keyActionBinding).showDialog(getTextGUI());
			}
		});

		actionGridActionBinding.put(AdhocGridAction.TOGGLE_KEY_BINDINGS_BAR, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				showingAllBindings = !showingAllBindings;
				statusLabel.setText(showingAllBindings ? buildAllBindingsText() : DEFAULT_STATUS);
			}
		});

		actionGridActionBinding.put(AdhocGridAction.MEMBER_SELECTION, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				MemberSelectionWindow msw = new MemberSelectionWindow(
						new EssGridMemberSelectionWindowModel(dataSource), point);
				msw.showDialog(getTextGUI());
			}
		});

		actionGridActionBinding.put(AdhocGridAction.ZOOM_OUT, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				// System.out.println("Zooming out on " + point);
				dataSource.zoomOut(point);
			}
		});

		actionGridActionBinding.put(AdhocGridAction.KEEP_ONLY, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				dataSource.keepOnly(point);
			}
		});

		actionGridActionBinding.put(AdhocGridAction.REMOVE_ONLY, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				dataSource.removeOnly(point);
			}
		});

		actionGridActionBinding.put(AdhocGridAction.RESET_GRID, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				dataSource.resetToDefault();
			}
		});

		actionGridActionBinding.put(AdhocGridAction.PIVOT, new GridAction() {
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

		actionGridActionBinding.put(AdhocGridAction.REDUCE_VISIBLE_COLUMNS, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				if (--visibleColumns < 1)
					visibleColumns = 1;
				grid.setVisibleColumns(visibleColumns);
			}
		});

		actionGridActionBinding.put(AdhocGridAction.INCREASE_VISIBLE_COLUMNS, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				++visibleColumns;
				grid.setVisibleColumns(visibleColumns);
			}
		});

		actionGridActionBinding.put(AdhocGridAction.EDIT_CELL, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				EssCell cell = gridData.getGrid().getCellData(point.getRow(), point.getCol());
				logger.info("Cell type: {}", cell.getCellType());
				if (cell.getCellType().equals(EssCellType.DATA)) {
					buildDataCellDialog(new DataCellActionCallback() {

						@Override
						public void enterCellData() {
							try {
								String value = new TextInputDialogBuilder()
										.setTitle("Enter Cell Value")
										.setDescription("Enter value")
										.build()
										.showDialog(getTextGUI());
								double numericValue = Double.valueOf(value);
								gridData.setData(point, numericValue);
							} catch (Exception e) {
								logger.error("Error setting data value: {}", e);
							}
						}

						@Override
						public void clearCell() {
							try {
								gridData.clearData(point);
							} catch (Exception e) {
								logger.error("Error clearing cell: {}", e);
							}

						}

					}).showDialog(getTextGUI());
				} else if (cell.getCellType().equals(EssCellType.MEMBER)) {
					buildMemberCellDialog(new MemberCellActionCallback() {
						@Override
						public void enterCellText() {
							String value = new TextInputDialogBuilder()
									.setTitle("Enter text")
									.setDescription("Enter text")
									.build()
									.showDialog(getTextGUI());
							logger.info("Got: {}", value);
						}});
				}

			}
		});

		actionGridActionBinding.put(AdhocGridAction.ADHOC_OPTIONS, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				AdhocOptionsDialogWindow dialog = new AdhocOptionsDialogWindow(options, dataSource.getSupportedOptions());
				dialog.showDialog(getTextGUI());
				if (dialog.isSaved()) {
					AdhocGridWindow.this.options = dialog.getValue();
					try {
						dataSource.updateCubeViewProperties(options);
						dataSource.retrieve();
					} catch (Exception e) {
						logger.error("Error applying ad hoc options: {}", e.getMessage(), e);
					}
				}
			}
		});

		actionGridActionBinding.put(AdhocGridAction.RUN_CALC, new GridAction() {
			@Override
			public void execute(Point point, EssGrid dataSource) {
				new ActionListDialogBuilder()
						.addActions(createCalcRuns())
						.build()
						.showDialog(getTextGUI());
			}
		});

		this.addWindowListener(new WindowListener() {

			@Override
			public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
				// seems to refer to inner content sizes
				logger.info("Resized to {}", newSize);
				grid.setVisibleRows(newSize.getRows() - 2);
				if (showingAllBindings) {
					statusLabel.setText(buildAllBindingsText());
				}
			}

			@Override
			public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {
				// TODO Auto-generated method stub

			}
		});

		setComponent(panel);
	}

	// Used only before the window has been shown/sized once (see currentWrapWidth) - a reasonable
	// guess for a typical terminal, not a hard limit.
	private static final int DEFAULT_BINDINGS_BAR_WRAP_WIDTH = 100;

	// Lanterna's Label never wraps on its own - it only ever breaks on an explicit "\n" - so with a
	// dozen-plus bindings the bar would otherwise render as one line far wider than any terminal and
	// just get clipped. Wrap by hand instead, breaking between whole entries (never mid-entry), at
	// whatever width the window is actually showing at right now, so a wide terminal gets it in as
	// few lines as it actually allows instead of an arbitrary conservative guess.
	private int currentWrapWidth() {
		TerminalSize size = getSize();
		return size != null && size.getColumns() > 10 ? size.getColumns() - 2 : DEFAULT_BINDINGS_BAR_WRAP_WIDTH;
	}

	private String buildAllBindingsText() {
		int wrapWidth = currentWrapWidth();
		StringBuilder text = new StringBuilder();
		int lineLength = 0;
		for (Map.Entry<KeyStroke, AdhocGridAction> entry : keyActionBinding.entrySet()) {
			String rendered = entry.getKey().getCharacter() + "=" + entry.getValue().getCode().replace('-', ' ');
			if (lineLength == 0) {
				text.append(rendered);
				lineLength = rendered.length();
			} else if (lineLength + 2 + rendered.length() > wrapWidth) {
				text.append(",\n").append(rendered);
				lineLength = rendered.length();
			} else {
				text.append(", ").append(rendered);
				lineLength += 2 + rendered.length();
			}
		}
		return text.toString();
	}

	public void refreshGrid() {
		// if
		// panel.removeComponent(grid);
		// System.out.println("Ref grid");
		String[] labels = new String[gridData.getGrid().getColumns()];
		for (int i = 1; i <= gridData.getGrid().getColumns(); i++) {
			labels[i - 1] = "Column " + i;
		}
		// grid = new EssTable<String>(labels);
		// grid.setKeyStrokeDelegate(this);
		TableModel<String> model = new TableModel<String>(labels);
		grid.setTableModel(model);

		for (int row = 0; row < gridData.getGrid().getRows(); row++) {
			List<String> rowVals = new ArrayList<String>();
			for (int col = 0; col < gridData.getGrid().getColumns(); col++) {
				EssCell cell = gridData.getGrid().getCellData(row, col);
				switch (cell.getCellType()) {
				case DATA:
					if (cell.isMissing()) {
						rowVals.add(options.getMissingLabel());
					} else {
						rowVals.add(formatter.format(cell.getDouble()));
					}
					break;
				case MEMBER:
					rowVals.add(cell.getValue());
					break;
				case OTHER:
					rowVals.add(cell.getValue());
					break;
				default:
					break;
				}

			}
			model.addRow(rowVals);
		}
	}

	@Override
	public boolean handleKeyStroke(KeyStroke keyStroke) {
		if (keyStroke.getKeyType().equals(KeyType.Character)) {
			Point point = Point.of(grid.getSelectedRow(), grid.getSelectedColumn());
			AdhocGridAction action = keyActionBinding.get(keyStroke);
			if (action != null) {
				GridAction gridAction = actionGridActionBinding.get(action);
				if (gridAction != null) {
					try {
						gridAction.execute(point, gridData);
						refreshGrid();
						// refreshGrid() swaps in a brand new TableModel; Lanterna preserves the
						// selected row/column index across that swap only while it's still in bounds
						// for the new grid - once an action shrinks the grid enough that it isn't
						// (e.g. zooming back out), the selection silently resets to (0, 0) instead of
						// clamping to the nearest valid cell. Restore it explicitly so the highlighted
						// cell stays where the user left it (or as close as the new grid allows).
						int newRows = gridData.getGrid().getRows();
						int newCols = gridData.getGrid().getColumns();
						grid.setSelectedRow(Math.min(point.getRow(), newRows - 1));
						grid.setSelectedColumn(Math.min(point.getCol(), newCols - 1));
					} catch (Exception e) {
						logger.error("Error performing {} at {}: {}", action, point, e.getMessage(), e);
					}
				}
				return true;
			}
		}
		return false;
	}

	public interface GridAction {

		public void execute(Point point, EssGrid dataSource);

	}

	public static class ZoomIn implements GridAction {
		public void execute(Point point, EssGrid dataSource) {
			// System.out.println("Zooming in on " + point);
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

					}
				})
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

	private interface DataCellActionCallback {

		public void enterCellData();

		public void clearCell();

	}

	private interface MemberCellActionCallback {

		public void enterCellText();

	}

	public static ActionListDialog buildMemberCellDialog(MemberCellActionCallback callback) {
		ActionListDialog actions = new ActionListDialogBuilder()
				.addAction("Enter text", new Runnable() {
					@Override
					public void run() {
						callback.enterCellText();
					}
				}).build();
		return actions;
	}

	public static ActionListDialog buildDataCellDialog(DataCellActionCallback callback) {
		ActionListDialog actions = new ActionListDialogBuilder()
				.addAction("Enter data", new Runnable() {
					@Override
					public void run() {
						logger.info("Wants to update data");
						callback.enterCellData();
					}
				})
				.addAction("Clear data", new Runnable() {
					@Override
					public void run() {
						logger.info("Clear the data in the cell");
						callback.clearCell();
					}

				})
				.setCanCancel(true)
				.setTitle("Data Cell Actions")
				.setDescription("Choose an action to perform on the selected data cell")
				.setListBoxSize(new TerminalSize(20, 4))
				.build();
		return actions;
	}

	protected Runnable[] createCalcRuns() {
		Collection<String> calcs = gridData.getCalcScripts();
		Runnable[] runnables = new Runnable[calcs.size() + 1];

		runnables[0] = new CalcRunnable("(default)");
		int index = 1;
		for (String calc : calcs) {
			runnables[index++] = new CalcRunnable(calc);
		}
		return runnables;
	}

	private class CalcRunnable implements Runnable {

		private String calcName;

		public CalcRunnable(String calcName) {
			this.calcName = calcName;
		}

		@Override
		public void run() {
			try {
				gridData.runCalc(calcName);
				logger.info("Finished calc");
			} catch (Exception e) {
				logger.error("Error running calc script");
			}
		}

		@Override
		public String toString() {
			return calcName;
		}

	}

}
