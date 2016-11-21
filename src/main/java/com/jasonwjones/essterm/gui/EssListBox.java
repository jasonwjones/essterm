package com.jasonwjones.essterm.gui;

import com.googlecode.lanterna.gui2.AbstractListBox;
import com.googlecode.lanterna.input.KeyStroke;

public class EssListBox<V> extends AbstractListBox<V, EssListBox<V>> {

	@Override
	public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
		switch (keyStroke.getKeyType()) {
		case Delete:
			
			//break;
		default:
			return super.handleKeyStroke(keyStroke);
		}

	}

}
