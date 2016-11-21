package com.jasonwjones.essterm.dialogs.adhocoptions;

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
import com.jasonwjones.essterm.grid.AdhocOptions.Indentation;

public class DisplayOptionsPanel extends Panel {
	
	private TextBox missingTextBox;
	
	private TextBox noAccessTextBox;
	
	public DisplayOptionsPanel(AdhocOptions options) {
		super();
				
		Panel indentation = new Panel();
		indentation.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		
		RadioBoxList<String> radioBoxList = new RadioBoxList<String>();
		radioBoxList.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
	    radioBoxList.addItem("None");
	    radioBoxList.addItem("Subitems");
	    radioBoxList.addItem("Totals");
		indentation.addComponent(radioBoxList.withBorder(Borders.singleLine("Indentation")));
		
		radioBoxList.addListener(new RadioBoxList.Listener() {
			@Override
			public void onSelectionChanged(int selectedIndex, int previousSelection) {
				switch (selectedIndex) {
				case 0:
					options.setIndentation(Indentation.NONE);
					break;
				case 1:
					options.setIndentation(Indentation.SUBITEMS);
					break;
				case 2:
					options.setIndentation(Indentation.TOTALS);
					break;
				}
			}
		});

		switch (options.getIndentation()) {
		case NONE:
			radioBoxList.setCheckedItemIndex(0);
			break;
		case SUBITEMS:
			radioBoxList.setCheckedItemIndex(1);
			break;
		case TOTALS:
			radioBoxList.setCheckedItemIndex(2);
			break;
		default:
			break;		
		}
		
		addComponent(indentation);

		CheckBoxList<String> suppress = new CheckBoxList<String>();
		suppress.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		suppress.addItem("#Missing Rows", options.isSuppressMissingRows());
		suppress.addItem("Zero Rows", options.isSuppressZeroRows());
		suppress.addItem("Underscore Characters", options.isSuppressUnderscores());
		addComponent(suppress.withBorder(Borders.singleLine("Suppress")));
		
		Panel replacement = new Panel(new GridLayout(2));
		replacement.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		replacement.addComponent(new Label("#Missing Label").setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));
		
		this.missingTextBox = new TextBox(options.getMissingLabel());
		replacement.addComponent(this.missingTextBox);
		replacement.addComponent(new Label("#No Access Label"));
		this.noAccessTextBox = new TextBox(options.getNoAccessLabel());
		replacement.addComponent(this.noAccessTextBox);
		addComponent(replacement.withBorder(Borders.singleLine("Replacement")));
		
		CheckBoxList<String> aliases = new CheckBoxList<String>();
		aliases.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill));
		aliases.addItem("Use aliases", options.isUseAliases());
		aliases.addItem("Use names and aliases for rows", options.isUseBothMemberNameAndAlias());
		aliases.addListener(new Listener() {
			@Override
			public void onStatusChanged(int itemIndex, boolean checked) {
				if (itemIndex == 0) {
					options.setUseAliases(checked);
				} else if (itemIndex == 1) {
					options.setUseBothMemberNameAndAlias(checked);
				}
			}});
		addComponent(aliases.withBorder(Borders.singleLine("Aliases")));
		
	}
	
	String getMissingText() {
		return missingTextBox.getText();
	}
	
	String getNoAccessText() {
		return noAccessTextBox.getText();
	}
	
}
