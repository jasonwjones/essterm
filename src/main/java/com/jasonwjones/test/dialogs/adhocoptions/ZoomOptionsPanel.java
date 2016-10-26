package com.jasonwjones.test.dialogs.adhocoptions;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;

public class ZoomOptionsPanel extends Panel {

	public ZoomOptionsPanel() {
		super();
		
		Panel zoomIn = new Panel();
		
		RadioBoxList<String> radioBoxList = new RadioBoxList<String>();
	    radioBoxList.addItem("Next Level");
	    radioBoxList.addItem("All Levels");
	    radioBoxList.addItem("Bottom Level");
	    radioBoxList.addItem("Sibling Level");
	    radioBoxList.addItem("Same Level");
	    radioBoxList.addItem("Same Generation");
	    radioBoxList.addItem("Formulas");
	    zoomIn.addComponent(radioBoxList.withBorder(Borders.singleLine("Zoom In")));
		addComponent(zoomIn);
		
		CheckBoxList<String> memberRetention = new CheckBoxList<String>();
		memberRetention.addItem("Include Selection");
		memberRetention.addItem("Within Selected Group");
		memberRetention.addItem("Remove Unselected Groups");
		
		addComponent(memberRetention.withBorder(Borders.singleLine("Member Retention")));
		
	}
	
}
