package com.jasonwjones.essterm.dialogs;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.ComboBox.Listener;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

public class MemberSelectionWindow extends BasicWindow {

	private static final Logger logger = LoggerFactory.getLogger(MemberSelectionWindow.class);
	
	private String currentDimension;
	
	private ComboBox<String> dimensionCombo;
	
	private CheckBoxList<String> availableMembers;
	
	//private MemberSelectionDelegate memberSelectionDelegate;
	
	public MemberSelectionWindow(MemberSelectionWindowDelegate delegate) {
		super("Member Selection");
		setCloseWindowWithEscape(true);
		setHints(Arrays.asList(Hint.EXPANDED));
		
		Panel panel = new Panel(new GridLayout(2).setHorizontalSpacing(1));
				
		Panel rightPanel = new Panel(new GridLayout(1).setVerticalSpacing(1).setTopMarginSize(1))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL));
		
		ActionListBox selectedMembers = new ActionListBox(new TerminalSize(15, 8));
		selectedMembers
			.setPreferredSize(new TerminalSize(30, 15))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true))
			.addItem("FY14", new Runnable() {
				@Override
				public void run() {
					logger.info("Selected");
				}})
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true))
			//.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill))
			.withBorder(Borders.singleLine("Selected Members"))
			.addTo(rightPanel);
	
		//Panel leftPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
		Panel leftPanel = new Panel(new GridLayout(1).setVerticalSpacing(1).setTopMarginSize(1))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL));
		
		this.dimensionCombo = new ComboBox<String>("Time", "Scenario")
			.setPreferredSize(new TerminalSize(28, 1))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, true))
			//.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill))
			.addListener(new Listener() {
				@Override
				public void onSelectionChanged(int selectedIndex, int previousSelection) {
					changedDimensionSelection(dimensionCombo.getItem(selectedIndex));
				}})
			.addTo(leftPanel);
		
		this.availableMembers = new CheckBoxList<String>();
		availableMembers
			.setPreferredSize(new TerminalSize(30, 10))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true))
			//.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill))
//			.addItem("FY15")
//			.addItem("FY16")
			.withBorder(Borders.singleLine("Dimension Members"))
			.addTo(leftPanel);
				
		RadioBoxList<String> direction = new RadioBoxList<String>(new TerminalSize(10, 2))
				.addItem("Across")
				.addItem("Down");

		direction
			//.withBorder(Borders.singleLine("Dimension Members"))
			.addTo(leftPanel);
		
		panel.addComponent(leftPanel).addComponent(rightPanel);
		
		//Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL).setSpacing(1));
		
		Panel buttonPanel = new Panel(new GridLayout(2))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER, true, false, 2, 1));
		
		buttonPanel.addComponent(new Button("Okay"));
		buttonPanel.addComponent(new Button("Cancel"));
		
		panel.addComponent(buttonPanel);
		
//        new Label("Test with the width of the label in questions and here goes")
//        .setLayoutData(
//                GridLayout.createLayoutData(
//                        GridLayout.Alignment.END,
//                        GridLayout.Alignment.CENTER,
//                        false,
//                        false,
//                        2,
//                        1))
//        .addTo(panel);
		
		
		setComponent(panel);
		
		setAvailableMembers(Arrays.asList("Cola", "Diet Cola with a pretty long name but really it's long", "Grape Soda"));
		
	}
	
    //@Override
    public Object showDialog(WindowBasedTextGUI textGUI) {
        //selectedFile = null;
        textGUI.addWindow(this);

        //Wait for the window to close, in case the window manager doesn't honor the MODAL hint
        waitUntilClosed();
        return null;
    	//super.add
        //super.showDialog(textGUI);
        //return selectedFile;
    }
	
	private void changedDimensionSelection(String dimensionName) {
		logger.info("Changed to dim: {}", dimensionName);
	}
	
	public void refresh() {
		
	}
	
	public interface MemberSelectionWindowDelegate {
		
		public void didChooseDimension(String dimension);
		
	}
	
	public void setDimensions(Collection<String> dimensions) {
		this.dimensionCombo.clearItems();
		for (String dimension : dimensions) {
			this.dimensionCombo.addItem(dimension);
		}
	}
	
	public void setAvailableMembers(Collection<String> availableMembers) {
		this.availableMembers.clearItems();
		for (String availableMember : availableMembers) {
			this.availableMembers.addItem(availableMember);
		}
	}	

}
