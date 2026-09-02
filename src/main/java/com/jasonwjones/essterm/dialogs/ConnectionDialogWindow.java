package com.jasonwjones.essterm.dialogs;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBox;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.RadioBoxList.Listener;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.jasonwjones.essterm.EssStringUtils;
import com.jasonwjones.essterm.essbase.ConnectionResolver;
import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.essterm.model.ChosenConnection.Backend;

public class ConnectionDialogWindow extends DialogWindow {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionDialogWindow.class);

	private ComboBox<String> serverComboBox;

	private Label serverLabel;

	private TextBox usernameTextBox;

	private TextBox passwordTextBox;

	private ActionListBox applicationsListBox;

	private RadioBoxList<String> cubesListBox;

	private Button loginButton;

	private CheckBox useRestCheckBox;

	private ConnectionResolver japiResolver;

	private ConnectionResolver restResolver;

	private ChosenConnection value;

	private ConnectionDialogModel model;

	private ConnectionResolver currentResolver() {
		return useRestCheckBox.isChecked() ? restResolver : japiResolver;
	}

	private Backend currentBackend() {
		return useRestCheckBox.isChecked() ? Backend.REST : Backend.JAPI;
	}

	public void setModel(ConnectionDialogModel model) {
		this.model = model;
		refreshServerList();
		usernameTextBox.setText(EssStringUtils.nullsafeString(model.getRecentUsername()));
		passwordTextBox.setText(EssStringUtils.nullsafeString(model.getRecentPassword()));
		passwordTextBox.setMask('*');
	}

	/**
	 * Repopulates the server combo box with recent servers for whichever backend is currently
	 * selected - JAPI servers and REST endpoint URLs are never valid interchangeably, so each
	 * backend keeps its own recent-server history rather than sharing one mixed list.
	 */
	private void refreshServerList() {
		if (model == null) {
			return;
		}
		Backend backend = currentBackend();
		String recentServer = model.getRecentServer(backend);

		serverComboBox.clearItems();
		if (recentServer != null) {
			serverComboBox.addItem(recentServer);
		}
		for (String server : model.getRecentServers(backend)) {
			if (!server.equals(recentServer)) {
				serverComboBox.addItem(server);
			}
		}
		if (serverComboBox.getItemCount() > 0) {
			serverComboBox.setSelectedIndex(0);
		}
	}
	
	public ConnectionDialogWindow() {
		this("Connect");
	}

	public ConnectionDialogWindow(String title) {
		super(title);

		value = new ChosenConnection();

		setCloseWindowWithEscape(true);

		serverComboBox = new ComboBox<String>("Item 1", "item 2");
		serverComboBox.setReadOnly(false);
		serverComboBox.setPreferredSize(new TerminalSize(40, 1));
		// skip listener on combo box since it can take arbitrary text

		loginButton = new Button("Login", new Runnable() {
			public void run() {
				try {
					value.setServer(serverComboBox.getText());
					value.setUsername(usernameTextBox.getText());
					value.setPassword(passwordTextBox.getText());
					refreshApplications();
					applicationsListBox.takeFocus();
				} catch (Exception e) {
					logger.error("Error logging in: {}", e);
					showException(e);
					
				}
			}
		});
		
		usernameTextBox = new TextBox(new TerminalSize(14, 1));
		passwordTextBox = new TextBox(new TerminalSize(14, 1));

		serverLabel = new Label("Server");

		useRestCheckBox = new CheckBox("Use REST API");
		useRestCheckBox.addListener(new CheckBox.Listener() {
			@Override
			public void onStatusChanged(boolean checked) {
				serverLabel.setText(checked ? "Server (endpoint URL)" : "Server");
				refreshServerList();
			}
		});

		// right side panel
		Panel rightPanel = new Panel();

		TerminalSize listSize = new TerminalSize(24, 8);
		
		cubesListBox = new RadioBoxList<String>(listSize);
		cubesListBox.addListener(new Listener() {
			@Override
			public void onSelectionChanged(int selectedIndex, int previousSelection) {
				//System.out.println("Selected cube: " + cubesListBox.getCheckedItem());
				String cubeName = cubesListBox.getCheckedItem();
				if (cubeName != null) {
					close();
				}

			}
		});

		rightPanel.addComponent(cubesListBox.withBorder(Borders.singleLine("Cubes")));

		// applicationsListBox = new RadioBoxList<String>(new TerminalSize(20,
		// 5));
		applicationsListBox = new ActionListBox(listSize);
		// applicationsListBox.addListener(new Listener() {
		//
		// @Override
		// public void onSelectionChanged(int selectedIndex, int
		// previousSelection) {
		// System.out.println("Selected app " +
		// applicationsListBox.getSelectedItem());
		// String application = applicationsListBox.getSelectedItem();
		//
		// cubesListBox.clearItems();
		//
		// for (String cube : model.getCubes("app")) {
		// cubesListBox.addItem(cube);
		// };
		// cubesListBox.takeFocus();
		// }
		// });

		//Panel containerPanel = new Panel();

		Panel topPanel = new Panel(new GridLayout(2).setVerticalSpacing(1).setTopMarginSize(1).setBottomMarginSize(1));
//		LayoutData layoutData = GridLayout.createLayoutData(Alignment.FILL, Alignment.BEGINNING, true, false, 2, 1);
//		topPanel.setLayoutData(layoutData);

//		Panel innerTopPanel = new Panel(
//				new GridLayout(2).setVerticalSpacing(1).setTopMarginSize(1).setBottomMarginSize(1));

		topPanel.addComponent(new Label("Backend"));
		topPanel.addComponent(useRestCheckBox);

		topPanel.addComponent(serverLabel);
		topPanel.addComponent(serverComboBox);

		topPanel.addComponent(new Label("Username"));
		topPanel.addComponent(usernameTextBox);
		
		topPanel.addComponent(new Label("Password"));
		
		Panel miniPanel = new Panel(new GridLayout(2).setLeftMarginSize(0)).addComponent(passwordTextBox).addComponent(loginButton);
		topPanel.addComponent(miniPanel);

		Panel leftPanel = new Panel();
		leftPanel.addComponent(applicationsListBox.withBorder(Borders.singleLine("Applications")));

		Panel containerPanel = new Panel();
		containerPanel.addComponent(topPanel);
		
		//containerPanel.addComponent(loginButton);
		
		Panel appsAndCubesPanel = new Panel(new GridLayout(2));
		appsAndCubesPanel.addComponent(leftPanel);
		
		
		
		appsAndCubesPanel.addComponent(rightPanel);
		containerPanel.addComponent(appsAndCubesPanel);

		setComponent(containerPanel);
	}

	private void refreshApplications() throws Exception {
		applicationsListBox.clearItems();

		for (String application : currentResolver().getApplications(value.getServer(), value.getUsername(), value.getPassword())) {
			applicationsListBox.addItem(application, new ApplicationRunnable(application));
		}
	}

	@Override
	public ChosenConnection showDialog(WindowBasedTextGUI textGUI) {
		super.showDialog(textGUI);
		return getChosenConnection();
	}

	public ChosenConnection getChosenConnection() {
		value.setServer(serverComboBox.getText());
		value.setUsername(usernameTextBox.getText());
		value.setPassword(passwordTextBox.getText());
		value.setBackend(useRestCheckBox.isChecked() ? Backend.REST : Backend.JAPI);
		// application is filled in when selected
		value.setCube(cubesListBox.getSelectedItem());
		return value;
	}

	
	
	private class ApplicationRunnable implements Runnable {

		private String application;

		public ApplicationRunnable(String application) {
			this.application = application;
		}

		@Override
		public void run() {
			cubesListBox.clearItems();
			try {
				for (String cube : currentResolver().getCubes(application)) {
					cubesListBox.addItem(cube);
				}
				ConnectionDialogWindow.this.value.setApplication(application);
				//logger.info("CDW: {}", ConnectionDialogWindow.this.value.getApplication());
				cubesListBox.takeFocus();

			} catch (Exception e) {
				logger.error("Error fetching cubes: {}", e);
			}
		}

	}

	public static interface ConnectionDialogModel {

		public Set<String> getRecentServers(Backend backend);

		public String getRecentServer(Backend backend);

		public String getRecentUsername();

		public String getRecentPassword();

	}

	public void setJapiResolver(ConnectionResolver japiResolver) {
		this.japiResolver = japiResolver;
	}

	public void setRestResolver(ConnectionResolver restResolver) {
		this.restResolver = restResolver;
	}

	public void showException(Exception e) {
		new MessageDialogBuilder()
			.setTitle("Error")
			.setText("There was an error logging in to Essbase.\n\n" + e.getMessage())
			.build()
			.showDialog(getTextGUI());
	}
	
}
