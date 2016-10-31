package com.jasonwjones.essterm;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;

public class EssTable<V> extends Table<V> {

	private static final Logger logger = LoggerFactory.getLogger(EssTable.class);
	
	private KeyStrokeDelegate keyStrokeDelegate;
	
	public EssTable(String... columnLabels) {
		super(columnLabels);
		setCellSelection(true);
	}

	@Override
	public Result handleKeyStroke(KeyStroke keyStroke) {
		//System.out.println("Key Stroke: " + keyStroke);
		if (keyStrokeDelegate != null) {
			if (keyStrokeDelegate.handleKeyStroke(keyStroke)) {
				return Result.HANDLED;
			} 
		} 
		logger.info("Sending key up the chain");
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

	// CONSULT ComboBox for notes on how a popup should work.
    private class PopupWindow extends BasicWindow {
        private final ActionListBox listBox;

        public PopupWindow(List<String> options) {
            setHints(Arrays.asList(
                    Hint.NO_FOCUS,
                    Hint.FIXED_POSITION));
            listBox = new ActionListBox(new TerminalSize(16, 4));
            for(int i = 0; i < options.size(); i++) {
                String item = options.get(i);
                final int index = i;
                listBox.addItem(item.toString(), new Runnable() {
                    @Override
                    public void run() {
                        //setSelectedIndex(index);
                        close();
                    }
                });
            }
            //listBox.setSelectedIndex(getSelectedIndex());
            setComponent(listBox);
        }

        @Override
        public synchronized Theme getTheme() {
        	return EssTable.this.getTheme();
        }
    }
	
}
