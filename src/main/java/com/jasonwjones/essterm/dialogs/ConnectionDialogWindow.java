package com.jasonwjones.essterm.dialogs;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essbase.api.base.EssException;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
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
import com.jasonwjones.essterm.essbase.EssbaseConnectionResolver;
import com.jasonwjones.essterm.model.ChosenConnection;

public class ConnectionDialogWindow extends DialogWindow {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionDialogWindow.class);

	private ComboBox<String> serverComboBox;

	private TextBox usernameTextBox;

	private TextBox passwordTextBox;

	private ActionListBox applicationsListBox;

	private RadioBoxList<String> cubesListBox;

	private Button loginButton;

	private EssbaseConnectionResolver essbaseResolver;

	private ChosenConnection value;

	public void setModel(ConnectionDialogModel model) {

		serverComboBox.clearItems();
		if (model.getRecentServer() != null) {
			serverComboBox.addItem(model.getRecentServer());
		}
		for (String recentServer : model.getRecentServers()) {
			if (!recentServer.equals(model.getRecentServer())) {
				serverComboBox.addItem(recentServer);	
			}
		}
		if (serverComboBox.getItemCount() > 0) {
			serverComboBox.setSelectedIndex(0);
		}
		usernameTextBox.setText(EssStringUtils.nullsafeString(model.getRecentUsername()));
		passwordTextBox.setText(EssStringUtils.nullsafeString(model.getRecentPassword()));
		passwordTextBox.setMask('*');
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
				} catch (EssException e) {
					logger.error("Error logging in: {}", e);
					showException(e);
					
				}
			}
		});
		
		usernameTextBox = new TextBox(new TerminalSize(14, 1));
		passwordTextBox = new TextBox(new TerminalSize(14, 1));

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

		topPanel.addComponent(new Label("Server"));
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

	private void refreshApplications() throws EssException {
		applicationsListBox.clearItems();

		for (String application : essbaseResolver.getApplications(value.getServer(), value.getUsername(), value.getPassword())) {
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
				for (String cube : essbaseResolver.getCubes(application)) {
					cubesListBox.addItem(cube);
				}
				ConnectionDialogWindow.this.value.setApplication(application);
				//logger.info("CDW: {}", ConnectionDialogWindow.this.value.getApplication());
				cubesListBox.takeFocus();

			} catch (EssException e) {
				logger.error("Error fetching cubes: {}", e);
			}
		}

	}

	public static interface ConnectionDialogModel {

		public Set<String> getRecentServers();
		
		public String getRecentServer();
		
		public String getRecentUsername();
		
		public String getRecentPassword();

	}

	public EssbaseConnectionResolver getEssbaseResolver() {
		return essbaseResolver;
	}

	public void setEssbaseResolver(EssbaseConnectionResolver essbaseResolver) {
		this.essbaseResolver = essbaseResolver;
	}

	public void showException(Exception e) {
		new MessageDialogBuilder()
			.setTitle("Error")
			.setText("There was an error logging in to Essbase.\n\n" + e.getMessage())
			.build()
			.showDialog(getTextGUI());
	}
	
}
