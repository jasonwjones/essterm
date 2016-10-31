package com.jasonwjones.test.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.jasonwjones.test.dialogs.adhocoptions.DisplayOptionsPanel;
import com.jasonwjones.test.dialogs.adhocoptions.GeneralOptionsPanel;
import com.jasonwjones.test.dialogs.adhocoptions.ZoomOptionsPanel;

public class AdhocOptionsDialogWindow extends DialogWindow {

	private Panel displayOptions;
	
	private Panel zoomOptions;
	
	private Panel generalOptions;
	
	private Panel rightContainer;
	
	public AdhocOptionsDialogWindow() {
		this("Ad hoc Options");
	}
	
	public AdhocOptionsDialogWindow(String title) {
		super(title);

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
		this.displayOptions = new DisplayOptionsPanel();
		//this.generalOptions.addComponent(new Label("First thing on general options)"));
		
		this.zoomOptions = new ZoomOptionsPanel();
		this.generalOptions = new GeneralOptionsPanel();
		
		rightContainer.addComponent(displayOptions);
		setComponent(splitPanel);
		
	}
	
}
