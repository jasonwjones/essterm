package com.jasonwjones.essterm.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.ComboBox.Listener;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Interactable.Result;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.jasonwjones.essterm.grid.EssMemberNode;
import com.jasonwjones.essterm.grid.MemberPlacement;
import com.jasonwjones.essterm.grid.Point;

/**
 * Lets someone browse a cube's dimensions (each shown as a lazily-expandable member tree, root
 * being the dimension itself) and place chosen members into the ad hoc grid starting at a given
 * cell, going down the same column or across the same row.
 */
public class MemberSelectionWindow extends BasicWindow {

	private static final Logger logger = LoggerFactory.getLogger(MemberSelectionWindow.class);

	private static final String ACROSS = "Across";

	private static final String DOWN = "Down";

	private ComboBox<String> dimensionCombo;

	private ActionListBox availableMembers;

	private ActionListBox selectedMembers;

	private RadioBoxList<String> direction;

	private MemberSelectionWindowModel model;

	private Point activeCell;

	private List<TreeRow> visibleRows = new ArrayList<>();

	private List<String> selectedMemberNames = new ArrayList<>();

	public MemberSelectionWindow(MemberSelectionWindowModel model, Point activeCell) {
		super("Member Selection");
		setCloseWindowWithEscape(true);
		setHints(Arrays.asList(Hint.EXPANDED));

		this.model = model;
		this.activeCell = activeCell;

		Panel panel = new Panel(new GridLayout(2).setHorizontalSpacing(1));

		Panel rightPanel = new Panel(new GridLayout(1).setVerticalSpacing(1).setTopMarginSize(1))
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL));

		selectedMembers = new ActionListBox(new TerminalSize(30, 10));
		selectedMembers
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true))
				.withBorder(Borders.singleLine("Selected Members"))
				.addTo(rightPanel);

		Panel leftPanel = new Panel(new GridLayout(1).setVerticalSpacing(1).setTopMarginSize(1))
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL));

		this.dimensionCombo = new ComboBox<String>()
				.setPreferredSize(new TerminalSize(28, 1))
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, true))
				.addListener(new Listener() {
					@Override
					public void onSelectionChanged(int selectedIndex, int previousSelection, boolean changedByUserInteraction) {
						if (selectedIndex > -1) {
							changedDimensionSelection(dimensionCombo.getSelectedItem());
						}
					}
				})
				.addTo(leftPanel);

		availableMembers = new MemberTreeBox(new TerminalSize(30, 8));
		availableMembers
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true))
				.withBorder(Borders.singleLine("Dimension Members (Right/Left to expand/collapse, Enter to add)"))
				.addTo(leftPanel);

		direction = new RadioBoxList<String>(new TerminalSize(10, 2))
				.addItem(ACROSS)
				.addItem(DOWN);
		direction.setCheckedItemIndex(1);
		direction.addTo(leftPanel);

		panel.addComponent(leftPanel).addComponent(rightPanel);

		Panel buttonPanel = new Panel(new GridLayout(2))
				.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER, true, false, 2, 1));

		buttonPanel.addComponent(new Button("Okay", new Runnable() {
			@Override
			public void run() {
				apply();
			}
		}));
		buttonPanel.addComponent(new Button("Cancel", new Runnable() {
			@Override
			public void run() {
				close();
			}
		}));

		panel.addComponent(buttonPanel);

		setComponent(panel);
		setModel(model);
	}

	public void showDialog(WindowBasedTextGUI textGUI) {
		textGUI.addWindowAndWait(this);
	}

	private void setModel(MemberSelectionWindowModel model) {
		dimensionCombo.clearItems();
		for (String dimension : model.getDimensions()) {
			dimensionCombo.addItem(dimension);
		}
		if (dimensionCombo.getItemCount() > 0) {
			dimensionCombo.setSelectedIndex(0);
		}
	}

	private void changedDimensionSelection(String dimensionName) {
		logger.info("Browsing dimension: {}", dimensionName);
		EssMemberNode root = model.getDimensionRoot(dimensionName);
		visibleRows = new ArrayList<>();
		visibleRows.add(new TreeRow(root, 0));
		refreshAvailableMembers();
	}

	private void refreshAvailableMembers() {
		availableMembers.clearItems();
		for (TreeRow row : visibleRows) {
			availableMembers.addItem(row.label(), new AddMember(row.node.getName()));
		}
	}

	private void expandRow(int index) {
		TreeRow row = visibleRows.get(index);
		if (row.node.isLeaf() || row.expanded) {
			return;
		}
		row.expanded = true;
		int insertAt = index + 1;
		for (EssMemberNode child : row.node.getChildren()) {
			visibleRows.add(insertAt++, new TreeRow(child, row.depth + 1));
		}
		refreshAvailableMembers();
		availableMembers.setSelectedIndex(index);
	}

	private void collapseRow(int index) {
		TreeRow row = visibleRows.get(index);
		if (!row.expanded) {
			return;
		}
		row.expanded = false;
		int removeFrom = index + 1;
		while (removeFrom < visibleRows.size() && visibleRows.get(removeFrom).depth > row.depth) {
			visibleRows.remove(removeFrom);
		}
		refreshAvailableMembers();
		availableMembers.setSelectedIndex(index);
	}

	private void refreshSelectedMembers() {
		selectedMembers.clearItems();
		for (String memberName : selectedMemberNames) {
			selectedMembers.addItem(memberName, new RemoveSelectedMember(memberName));
		}
	}

	private void apply() {
		if (selectedMemberNames.isEmpty()) {
			new MessageDialogBuilder()
					.setTitle("Member Selection")
					.setText("Select at least one member before continuing.")
					.build()
					.showDialog(getTextGUI());
			return;
		}

		List<MemberPlacement> placements = new ArrayList<>();
		boolean down = DOWN.equals(direction.getCheckedItem());
		for (int i = 0; i < selectedMemberNames.size(); i++) {
			Point point = down
					? Point.of(activeCell.getRow() + i, activeCell.getCol())
					: Point.of(activeCell.getRow(), activeCell.getCol() + i);
			placements.add(new MemberPlacement(point, selectedMemberNames.get(i)));
		}
		model.setMembers(placements);
		close();
	}

	private class AddMember implements Runnable {

		private final String memberName;

		AddMember(String memberName) {
			this.memberName = memberName;
		}

		@Override
		public void run() {
			selectedMemberNames.add(memberName);
			refreshSelectedMembers();
		}

	}

	private class RemoveSelectedMember implements Runnable {

		private final String memberName;

		RemoveSelectedMember(String memberName) {
			this.memberName = memberName;
		}

		@Override
		public void run() {
			selectedMemberNames.remove(memberName);
			refreshSelectedMembers();
		}

	}

	/**
	 * An {@link ActionListBox} rendering {@link #visibleRows} as an indented tree: Enter runs the
	 * row's normal action (adding it to the selection), while Right/Left expand or collapse the
	 * currently-selected row in place.
	 */
	private class MemberTreeBox extends ActionListBox {

		MemberTreeBox(TerminalSize size) {
			super(size);
		}

		@Override
		public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
			int index = getSelectedIndex();
			if (index >= 0 && index < visibleRows.size()) {
				TreeRow row = visibleRows.get(index);
				if (keyStroke.getKeyType() == KeyType.ArrowRight && !row.node.isLeaf() && !row.expanded) {
					expandRow(index);
					return Result.HANDLED;
				}
				if (keyStroke.getKeyType() == KeyType.ArrowLeft && row.expanded) {
					collapseRow(index);
					return Result.HANDLED;
				}
			}
			return super.handleKeyStroke(keyStroke);
		}

	}

	private static class TreeRow {

		private final EssMemberNode node;

		private final int depth;

		private boolean expanded;

		TreeRow(EssMemberNode node, int depth) {
			this.node = node;
			this.depth = depth;
		}

		String label() {
			String prefix = node.isLeaf() ? "  " : (expanded ? "- " : "+ ");
			return "  ".repeat(depth) + prefix + node.getName();
		}

	}

	public interface MemberSelectionWindowModel {

		List<String> getDimensions();

		EssMemberNode getDimensionRoot(String dimensionName);

		void setMembers(List<MemberPlacement> placements);

	}

}
