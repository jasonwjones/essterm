package com.jasonwjones.essterm.dialogs;

import java.util.Arrays;
import java.util.Map;

import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.input.KeyStroke;

public class KeyBindingsWindow extends DialogWindow {

	private Table<String> keyBindingsTable;
	
	protected KeyBindingsWindow(Map<KeyStroke, AdhocGridAction> keyBindings) {
		super("Key Bindings");
		setHints(Arrays.asList(Hint.CENTERED, Hint.MODAL));
		
		setCloseWindowWithEscape(true);
		
		keyBindingsTable = new Table<String>("Key", "Action");
		
		TableModel<String> model = keyBindingsTable.getTableModel();
		
		for (Map.Entry<KeyStroke, AdhocGridAction> entry : keyBindings.entrySet()) {
			model.addRow(entry.getKey().getCharacter().toString(), entry.getValue().getDescription());
		}
		
		setComponent(keyBindingsTable);
	}


	
	
	
	
}
