package com.jasonwjones.essterm.dialogs;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.GridLayout.Alignment;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

public class AboutDialog extends DialogWindow {

	private static final Logger logger = LoggerFactory.getLogger(AboutDialog.class);
	
	protected AboutDialog(String title) {
		super(title);

		setHints(Arrays.asList(Hint.CENTERED));
		
		Panel panel = new Panel(new GridLayout(1)
				.setVerticalSpacing(1)
				.setBottomMarginSize(1)
				.setLeftMarginSize(1)
				.setRightMarginSize(1));

		try {
			String about = StreamUtils.copyToString(getClass().getResourceAsStream("/essterm.txt"),
					Charset.forName("UTF-8"));

			new Label(about)
					.setLayoutData(GridLayout.createLayoutData(Alignment.CENTER, Alignment.CENTER))
					.addTo(panel);
		} catch (Exception e) {
			logger.error("Couldn't load resource: {}", e.getMessage());
		}

		panel.addComponent(new Label("Version: 1.0.0"));
		panel.addComponent(new Label("Developed by Jason Jones"));
		
		panel.addComponent(new Button("Okay", new Runnable() {
			@Override
			public void run() {
				close();
			}
		}).setLayoutData(GridLayout.createLayoutData(Alignment.CENTER, Alignment.CENTER)));

		setComponent(panel);
	}

}
