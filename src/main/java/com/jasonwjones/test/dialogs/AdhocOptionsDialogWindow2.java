package com.jasonwjones.test.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.jasonwjones.test.dialogs.adhocoptions.DisplayOptionsPanel;

public class AdhocOptionsDialogWindow2 extends DialogWindow {

	private Panel generalOptions;
	
	private Panel zoomOptions;
	
	private Panel rightContainer;
	
	public AdhocOptionsDialogWindow2() {
		this("Ad hoc Options");
	}
	
	public AdhocOptionsDialogWindow2(String title) {
		super(title);

		setCloseWindowWithEscape(true);
		
		Panel splitPanel = new Panel(new GridLayout(2));

		ActionListBox optionGroups = new ActionListBox(new TerminalSize(12, 4));
		optionGroups.addItem("General", new Runnable() {
			public void run() {
				rightContainer.removeAllComponents();
				rightContainer.addComponent(generalOptions);
				getTextGUI().addWindowAndWait(new ConnectionDialogWindow("Hey"));
			}});
		
		optionGroups.addItem("Zoom", new Runnable() {
			public void run() {
				rightContainer.removeAllComponents();
				rightContainer.addComponent(zoomOptions);
				
				
				
			}});
		
		this.rightContainer = new Panel();
		
		splitPanel.addComponent(optionGroups.withBorder(Borders.singleLine("Category")));
		splitPanel.addComponent(this.rightContainer);
	
		
		
		//this.generalOptions = new Panel();
		this.generalOptions = new DisplayOptionsPanel();
		//this.generalOptions.addComponent(new Label("First thing on general options)"));
		
		this.zoomOptions = new Panel();
		this.zoomOptions.addComponent(new Label("Looking at zoom options"));
		
		rightContainer.addComponent(generalOptions);
		setComponent(splitPanel);
		
	}
	
}
