package com.jasonwjones.essterm.dialogs.adhocoptions;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;

public class GeneralOptionsPanel extends Panel {

	public GeneralOptionsPanel() {		
		super();
		
		Panel zoomIn = new Panel();

		RadioBoxList<String> modeRadios = new RadioBoxList<String>();
		modeRadios.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		modeRadios.addItem("Navigate without Data");

		zoomIn.addComponent(modeRadios.withBorder(Borders.singleLine("Indentation")));
		addComponent(modeRadios);

	}
	
}
