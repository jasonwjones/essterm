package com.jasonwjones.essterm.dialogs.adhocoptions;

import java.util.EnumSet;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.GridLayout.Alignment;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.jasonwjones.essterm.grid.AdhocOptionCapability;
import com.jasonwjones.essterm.grid.AdhocOptions;

/**
 * Mirrors the classic Essbase ad hoc "Options" dialog, to the extent a terminal UI and the current
 * connection can support it. Only the Display and Zoom tabs are here - the classic dialog's Mode,
 * Style, and Global tabs are almost entirely Excel/Smart View concepts (fonts and colors for cell
 * classes, "use sheet options with Query Designer", and the like) with no terminal equivalent, so
 * they're left out rather than built as permanently-dead UI.
 *
 * <p>Edits happen against a working copy of the given {@link AdhocOptions} - nothing is written back
 * to the original until {@link #isSaved()} is true after the dialog closes, so Cancel genuinely
 * discards whatever was changed.
 */
public class AdhocOptionsDialogWindow extends DialogWindow {

	private DisplayOptionsPanel displayOptions;

	private ZoomOptionsPanel zoomOptions;

	private Panel rightContainer;

	private final AdhocOptions value;

	private boolean saved;

	public AdhocOptionsDialogWindow(AdhocOptions original, EnumSet<AdhocOptionCapability> supported) {
		super("Ad hoc Options");

		this.value = new AdhocOptions(original);

		this.displayOptions = new DisplayOptionsPanel(value, supported);
		this.zoomOptions = new ZoomOptionsPanel(value, supported);

		Panel splitPanel = new Panel(new GridLayout(2));

		ActionListBox optionGroups = new ActionListBox(new TerminalSize(12, 4));
		optionGroups.addItem("Display", new PanelSwitcher(displayOptions));
		optionGroups.addItem("Zoom", new PanelSwitcher(zoomOptions));

		this.rightContainer = new Panel();

		splitPanel.addComponent(optionGroups.withBorder(Borders.singleLine("Category")));
		splitPanel.addComponent(this.rightContainer);

		rightContainer.addComponent(displayOptions);

		Panel bottomPanel = new Panel(new LinearLayout(Direction.HORIZONTAL))
				.setLayoutData(GridLayout.createLayoutData(Alignment.END, Alignment.CENTER, true, false, 2, 1));

		bottomPanel.addComponent(new Button("Save", new Runnable() {
			@Override
			public void run() {
				value.setMissingLabel(displayOptions.getMissingText());
				value.setNoAccessLabel(displayOptions.getNoAccessText());
				saved = true;
				close();
			}
		}));

		bottomPanel.addComponent(new Button("Cancel", new Runnable() {
			@Override
			public void run() {
				close();
			}
		}));

		splitPanel.addComponent(bottomPanel);

		setComponent(splitPanel);
	}

	/**
	 * The edited options, if {@link #isSaved()} - discard otherwise.
	 */
	public AdhocOptions getValue() {
		return value;
	}

	/**
	 * True if the dialog was closed via Save rather than Cancel or Escape.
	 */
	public boolean isSaved() {
		return saved;
	}

	private class PanelSwitcher implements Runnable {

		private Panel panel;

		public PanelSwitcher(Panel panel) {
			this.panel = panel;
		}

		@Override
		public void run() {
			rightContainer.removeAllComponents();
			rightContainer.addComponent(this.panel);
		}

	}

}
