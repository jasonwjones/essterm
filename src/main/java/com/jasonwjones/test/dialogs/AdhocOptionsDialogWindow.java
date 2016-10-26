package com.jasonwjones.test.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

public class AdhocOptionsDialogWindow extends DialogWindow {

	private final TextBox textBox;

	public AdhocOptionsDialogWindow() {
		super("Ad Hoc Options");
		setSize(new TerminalSize(20,  5));

		this.textBox = new TextBox(new TerminalSize(30, 1), "Hi there");

		Panel optionPanel = new Panel();
		
		optionPanel.setLayoutManager(
                new GridLayout(1)
                .setLeftMarginSize(1)
                .setRightMarginSize(1)
                .setTopMarginSize(1)
                .setBottomMarginSize(1));
		
		optionPanel.addComponent(textBox);
		optionPanel.addComponent(new Button("Close", new Runnable() {
			public void run() {
				onClose();
			}
		}));
		
		
		
		setComponent(optionPanel);
	}

	private void onClose() {
		close();
	}

}
