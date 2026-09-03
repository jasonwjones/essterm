package com.jasonwjones.essterm.dialogs.adhoc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.googlecode.lanterna.input.KeyStroke;
import com.jasonwjones.essterm.dialogs.AdhocGridAction;

public class KeyBindingManager {
	
	public static Map<KeyStroke, AdhocGridAction> defaultKeyBindings() {
		// LinkedHashMap so the bindings bar (AdhocGridWindow.buildAllBindingsText) lists these in a
		// stable, sensible order instead of HashMap's arbitrary one.
		Map<KeyStroke, AdhocGridAction> defaultBindings = new LinkedHashMap<>();

		defaultBindings.put(charKey('a'), AdhocGridAction.ZOOM_IN);
		defaultBindings.put(charKey('A'), AdhocGridAction.ZOOM_IN_INCLUDE_SELECTION);
		defaultBindings.put(charKey('s'), AdhocGridAction.ZOOM_OUT);
		defaultBindings.put(charKey('q'), AdhocGridAction.KEEP_ONLY);
		defaultBindings.put(charKey('w'), AdhocGridAction.REMOVE_ONLY);
		defaultBindings.put(charKey('!'), AdhocGridAction.RUN_CALC);
		defaultBindings.put(charKey('v'), AdhocGridAction.PIVOT);
		defaultBindings.put(charKey('p'), AdhocGridAction.DATA_CELL_ACTION);
		defaultBindings.put(charKey('o'), AdhocGridAction.ADHOC_OPTIONS);
		defaultBindings.put(charKey('?'), AdhocGridAction.KEY_BINDING_OPTIONS);
		defaultBindings.put(charKey('m'), AdhocGridAction.MEMBER_SELECTION);
		defaultBindings.put(charKey('k'), AdhocGridAction.TOGGLE_KEY_BINDINGS_BAR);
		defaultBindings.put(charKey('r'), AdhocGridAction.RESET_GRID);

		return defaultBindings;
	}

	protected static KeyStroke charKey(char key) {
		return new KeyStroke(key, false, false);
	}

	public void loadKeyBindings(Properties properties) {
		
		for (AdhocGridAction action : AdhocGridAction.values()) {
			
		}
		
		//properties.getProperty(key)
	}
	
}
