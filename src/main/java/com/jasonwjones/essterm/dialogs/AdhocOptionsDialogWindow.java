package com.jasonwjones.essterm.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.jasonwjones.essterm.dialogs.adhocoptions.DisplayOptionsPanel;
import com.jasonwjones.essterm.dialogs.adhocoptions.GeneralOptionsPanel;
import com.jasonwjones.essterm.dialogs.adhocoptions.ZoomOptionsPanel;
import com.jasonwjones.essterm.grid.AdhocOptions;

public class AdhocOptionsDialogWindow extends DialogWindow {

	private Panel displayOptions;
	
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

		setCloseWindowWithEscape(true);
		
		Panel splitPanel = new Panel(new GridLayout(2));

		ActionListBox optionGroups = new ActionListBox(new TerminalSize(12, 4));
		optionGroups.addItem("Display", new Runnable() {
			public void run() {
				rightContainer.removeAllComponents();
				rightContainer.addComponent(displayOptions);
				getTextGUI().addWindowAndWait(new ConnectionDialogWindow("Hey"));
			}});
		
		optionGroups.addItem("Zoom", new Runnable() {
			public void run() {
				rightContainer.removeAllComponents();
				rightContainer.addComponent(zoomOptions);		
			}});
		
		optionGroups.addItem("General", new Runnable() {
			@Override
			public void run() {
				rightContainer.removeAllComponents();
				rightContainer.addComponent(generalOptions);
			}});
		
		this.rightContainer = new Panel();
		
		splitPanel.addComponent(optionGroups.withBorder(Borders.singleLine("Category")));
		splitPanel.addComponent(this.rightContainer);
	
		
		
		//this.generalOptions = new Panel();
		this.displayOptions = new DisplayOptionsPanel(value);
		//this.generalOptions.addComponent(new Label("First thing on general options)"));
		
		this.zoomOptions = new ZoomOptionsPanel();
		this.generalOptions = new GeneralOptionsPanel();
		
		rightContainer.addComponent(displayOptions);
		setComponent(splitPanel);
		
	}
	
}
