package com.jasonwjones.essterm.dialogs.adhoc;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.googlecode.lanterna.input.KeyStroke;
import com.jasonwjones.essterm.dialogs.AdhocGridAction;

public class KeyBindingManager {
	
	public static Map<KeyStroke, AdhocGridAction> defaultKeyBindings() {
		Map<KeyStroke, AdhocGridAction> defaultBindings = new HashMap<>();

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
