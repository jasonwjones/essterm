package com.jasonwjones.test;

import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;

public class EssTable<V> extends Table<V> {

	private KeyStrokeDelegate keyStrokeDelegate;
	
	public EssTable(String... columnLabels) {
		super(columnLabels);
		setCellSelection(true);
	}

	@Override
	public Result handleKeyStroke(KeyStroke keyStroke) {
		System.out.println("Key Stroke: " + keyStroke);
		if (keyStrokeDelegate != null) {
			if (keyStrokeDelegate.handleKeyStroke(keyStroke)) {
				return Result.HANDLED;
			} 
		} 
		return super.handleKeyStroke(keyStroke);
	}

	public static interface KeyStrokeDelegate {
		
		public boolean handleKeyStroke(KeyStroke keyStroke);
		
	}



	public KeyStrokeDelegate getKeyStrokeDelegate() {
		return keyStrokeDelegate;
	}

	public void setKeyStrokeDelegate(KeyStrokeDelegate keyStrokeDelegate) {
		this.keyStrokeDelegate = keyStrokeDelegate;
	}
}
