package com.jasonwjones.test.dialogs;

import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

public class AboutDialog extends DialogWindow {

	protected AboutDialog(String title) {
		super(title);
		
		Panel panel = new Panel();
			
		try {
			String about = StreamUtils.copyToString(getClass().getResourceAsStream("/essterm.txt"), Charset.forName("UTF-8"));
			Label text = new Label(about);
			panel.addComponent(text);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		panel.addComponent(new Button("Okay", new Runnable() {
			@Override
			public void run() {
				close();
			}}));
		
		setComponent(panel);
	}

}
