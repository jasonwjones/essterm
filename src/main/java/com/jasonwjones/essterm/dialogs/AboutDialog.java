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
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;

public class AboutDialog extends DialogWindow {

	private static final Logger logger = LoggerFactory.getLogger(AboutDialog.class);

	private static final String GITHUB_URL = "https://github.com/jasonwjones/essterm";

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

		centered(panel, new Label("A terminal-based Essbase ad hoc client"));
		centered(panel, new Label("Version: " + version()));
		centered(panel, new Label("Developed by Jason Jones"));
		centered(panel, new Label(GITHUB_URL));
		centered(panel, new Label("Essbase Java API: " + (japiAvailable() ? "available" : "not available (REST-only build)")));

		panel.addComponent(new Button("Okay", new Runnable() {
			@Override
			public void run() {
				close();
			}
		}).setLayoutData(GridLayout.createLayoutData(Alignment.CENTER, Alignment.CENTER)));

		setComponent(panel);
	}

	private static void centered(Panel panel, Label label) {
		panel.addComponent(label.setLayoutData(GridLayout.createLayoutData(Alignment.CENTER, Alignment.CENTER)));
	}

	// The packaged jar's manifest carries this (Spring Boot's own build sets Implementation-Version
	// from the POM automatically) - running unpackaged (an IDE, or "mvn spring-boot:run") has no
	// manifest to read, hence the fallback.
	private static String version() {
		String version = AboutDialog.class.getPackage().getImplementationVersion();
		return version != null ? version : "development build";
	}

	// Same "japi" Maven profile check as the Connect dialog's availability logic - true only if the
	// Oracle Essbase JAPI JARs were on the compile/runtime classpath (see pom.xml's "japi" profile).
	private static boolean japiAvailable() {
		try {
			Class.forName("com.essbase.api.session.IEssbase", false, AboutDialog.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

}
