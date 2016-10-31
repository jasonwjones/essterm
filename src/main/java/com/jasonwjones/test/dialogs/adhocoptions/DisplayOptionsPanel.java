package com.jasonwjones.test.dialogs.adhocoptions;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.CheckBoxList.Listener;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.TextBox;
import com.jasonwjones.essterm.grid.AdhocOptions;

public class DisplayOptionsPanel extends Panel {

	private AdhocOptions options;
	
	public DisplayOptionsPanel(AdhocOptions options) {
		super();
		
		this.options = options;
		//setLayoutData();
		//setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1));
		
		Panel indentation = new Panel();
		indentation.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		
		RadioBoxList<String> radioBoxList = new RadioBoxList<String>();
		radioBoxList.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
	    radioBoxList.addItem("None");
	    radioBoxList.addItem("Subitems");
	    radioBoxList.addItem("Totals");
		indentation.addComponent(radioBoxList.withBorder(Borders.singleLine("Indentation")));
		addComponent(indentation);
		
		CheckBoxList<String> suppress = new CheckBoxList<String>();
		suppress.addItem("#Missing Rows");
		suppress.addItem("Zero Rows");
		suppress.addItem("Underscore Characters");
		addComponent(suppress.withBorder(Borders.singleLine("Suppress")));
		
		Panel replacement = new Panel(new GridLayout(2));
		replacement.addComponent(new Label("#Missing Label"));
		replacement.addComponent(new TextBox());
		replacement.addComponent(new Label("#No Access Label"));
		replacement.addComponent(new TextBox());
		addComponent(replacement.withBorder(Borders.singleLine("Replacement")));
		
		CheckBoxList<String> aliases = new CheckBoxList<String>();
		aliases.addItem("Use Aliases", options.isUseAliases());
		aliases.addItem("Use Both Member Names and Aliases for row dimensions");
		aliases.addListener(new Listener() {
			@Override
			public void onStatusChanged(int itemIndex, boolean checked) {
				if (itemIndex == 0) {
					options.setUseAliases(checked);
				}
			}});
		addComponent(aliases.withBorder(Borders.singleLine("Suppress")));
		
	}
	
}
