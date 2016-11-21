package com.jasonwjones.essterm.dialogs.adhocoptions;

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
import com.jasonwjones.essterm.grid.AdhocOptions;

public class AdhocOptionsDialogWindow extends DialogWindow {

	private DisplayOptionsPanel displayOptions;
	
	private Panel zoomOptions;
	
	private Panel generalOptions;
	
	private Panel rightContainer;
	
	public AdhocOptions value;
	
	public AdhocOptionsDialogWindow() {
		this(new AdhocOptions());
	}
	
	public AdhocOptions getValue() {
		return value;
	}
	
	public AdhocOptionsDialogWindow(AdhocOptions value) {
		super("Ad hoc Options");

		//setCloseWindowWithEscape(true);
		
		this.displayOptions = new DisplayOptionsPanel(value);
		this.zoomOptions = new ZoomOptionsPanel();
		this.generalOptions = new GeneralOptionsPanel();
		
		Panel splitPanel = new Panel(new GridLayout(2));
	
		ActionListBox optionGroups = new ActionListBox(new TerminalSize(12, 4));
		optionGroups.addItem("Display", new PanelSwitcher(displayOptions));
		optionGroups.addItem("Zoom", new PanelSwitcher(zoomOptions));
		optionGroups.addItem("General", new PanelSwitcher(generalOptions));
				
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
				close();
			}}));
		
		//bottomPanel.addComponent(new Button("Cancel"));
		
		splitPanel.addComponent(bottomPanel);
		
		setComponent(splitPanel);
		
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
