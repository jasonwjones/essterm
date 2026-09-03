package com.jasonwjones.essterm.dialogs;

import java.util.Arrays;
import java.util.Collection;
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
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

public class MemberSelectionWindow extends BasicWindow {

	private static final Logger logger = LoggerFactory.getLogger(MemberSelectionWindow.class);
		
	private ComboBox<String> dimensionCombo;
	
	private ActionListBox availableMembers;
	
	private ActionListBox selectedMembers;
	
	private MemberSelectionWindowModel model;
	
	public MemberSelectionWindow(MemberSelectionWindowModel delegate) {
		super("Member Selection");
		setCloseWindowWithEscape(true);
		setHints(Arrays.asList(Hint.EXPANDED));
		
		this.model = delegate;
		
		Panel panel = new Panel(new GridLayout(2).setHorizontalSpacing(1));
				
		Panel rightPanel = new Panel(new GridLayout(1).setVerticalSpacing(1).setTopMarginSize(1))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL));		
		
		selectedMembers = new ActionListBox(new TerminalSize(15, 8));
		selectedMembers
			.setPreferredSize(new TerminalSize(30, 10))
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
		
		this.dimensionCombo = new ComboBox<String>()
			.setPreferredSize(new TerminalSize(28, 1))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.CENTER, true, true))
			//.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill))
			.addListener(new Listener() {
				@Override
				public void onSelectionChanged(int selectedIndex, int previousSelection, boolean changedByUserInteraction) {
					if (selectedIndex > -1) {
						changedDimensionSelection(dimensionCombo.getSelectedItem());
					}
				}})
			.addTo(leftPanel);
		
		this.availableMembers = new ActionListBox();
		availableMembers
			.setPreferredSize(new TerminalSize(30, 8))
			.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.FILL, GridLayout.Alignment.FILL, true, true))
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
		setModel(model);
	
	}
	
	public MemberSelection getValue() {
		return null;
		//selectedMembers.getI
		// just going to have to cast Runnable
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
		refreshAvailableMembers();
	}
		
	public interface MemberSelectionWindowModel {
		
		public Collection<String> getDimensions();
		
		public Collection<String> getMembers(String dimension);
				
	}
	
	public static class MemberSelection {
		
		private boolean down;
		
		private List<String> members;

		public boolean isDown() {
			return down;
		}

		public void setDown(boolean down) {
			this.down = down;
		}

		public List<String> getMembers() {
			return members;
		}

		public void setMembers(List<String> members) {
			this.members = members;
		}
		
	}
	
	public void setModel(MemberSelectionWindowModel model) {
		dimensionCombo.clearItems();
		
		for (String dimension : model.getDimensions()) {
			dimensionCombo.addItem(dimension);
		}
		dimensionCombo.setSelectedIndex(0);
		refreshAvailableMembers();
	}
	
	public void refreshAvailableMembers() {
		availableMembers.clearItems();
		for (String member : model.getMembers(dimensionCombo.getSelectedItem())) {
			availableMembers.addItem(member, new AvailableMember(member));
		}
	}
	
	public void setDimensions(Collection<String> dimensions) {
		this.dimensionCombo.clearItems();
		for (String dimension : dimensions) {
			this.dimensionCombo.addItem(dimension);
		}
	}
	
//	public void setAvailableMembers(Collection<String> availableMembers) {
//		this.availableMembers.clearItems();
//		for (String availableMember : availableMembers) {
//			this.availableMembers.addItem(availableMember);
//		}
//	}	

	private class AvailableMember implements Runnable {

		private String memberName;
		
		public AvailableMember(String memberName) {
			this.memberName = memberName;
		}
		
		@Override
		public void run() {
			selectedMembers.addItem(memberName, new Runnable() {
				@Override
				public void run() {
				}});
			logger.info("Selected: {}", memberName);
		}
		
		
	}
	
	
	
}
