package com.jasonwjones.essterm;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.jasonwjones.essterm.dialogs.AdhocGridWindow;
import com.jasonwjones.essterm.dialogs.ConnectionDialogWindow;
import com.jasonwjones.essterm.dialogs.ConnectionDialogWindow.ConnectionDialogModel;
import com.jasonwjones.essterm.dialogs.LauncherWindow;
import com.jasonwjones.essterm.dialogs.LauncherWindow.LauncherWindowDelegate;
import com.jasonwjones.essterm.dialogs.adhocoptions.AdhocOptionsDialogWindow;
import com.jasonwjones.essterm.essbase.EssbaseConnectionResolver;
import com.jasonwjones.essterm.essgrid.EssbaseEssGridFactory;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGridFactory;
import com.jasonwjones.essterm.model.ChosenConnection;

public class EssTerm implements LauncherWindowDelegate {

	private static final Logger logger = LoggerFactory.getLogger(EssTerm.class);

	@Autowired
	private WindowBasedTextGUI gui;

	@Autowired
	private ConnectionManager connectionManager;

	@Autowired
	private SettingsManager settingsManager;

	@Autowired
	private EssbaseConnectionResolver connectionResolver;

	public static void main(String[] args) {
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(EssTermConfig.class)) {
			EssTerm essTerm = context.getBean(EssTerm.class);
			essTerm.run();
		}
	}

	public void run() {
		LauncherWindow launcherWindow = new LauncherWindow();
		launcherWindow.setDelegate(this);
		gui.addWindowAndWait(launcherWindow);
	}

	@Override
	public void chooseConnection() {
		ConnectionDialogWindow connectionDialog = new ConnectionDialogWindow();
		connectionDialog.setModel(new SettingsManagerAdapter());
		connectionDialog.setEssbaseResolver(connectionResolver);

		ChosenConnection chosen = connectionDialog.showDialog(gui);
		this.connectionManager.setCurrentConnection(chosen);

		this.settingsManager.addRecentlyUsedServer(chosen.getServer());
		this.settingsManager.setRecentUsername(chosen.getUsername());
		this.settingsManager.setRecentPassword(chosen.getPassword());
		try {
			this.settingsManager.saveSettings();
		} catch (IOException e) {
			logger.warn("Error saving settings: {}", e.getMessage());
		}
	}

	@Override
	public void startAdhocGrid() {
		logger.info("Starting a new ad hoc grid");
		try {
			if (connectionManager.hasConnection()) {
				EssGridFactory gridFactory = new EssbaseEssGridFactory();
				EssGrid grid = gridFactory.createEssGrid(connectionManager.getCurrentConnection());

				grid.updateCubeViewProperties(settingsManager.getAdhocOptions());

				// perform initial retrieve so we have some data
				grid.retrieve();

				AdhocGridWindow gridWindow = new AdhocGridWindow(grid, settingsManager.getAdhocOptions());
				gui.addWindowAndWait(gridWindow);
			} else {
				logger.info("Must specify connection");
				new MessageDialogBuilder()
						.setTitle("Essterm")
						.setText("Specify a connection before creating a grid")
						.build().showDialog(gui);
			}
		} catch (Exception e) {
			logger.error("Problem starting grid: {}", e.getMessage(), e);
		}
	}

	private class SettingsManagerAdapter implements ConnectionDialogModel {

		@Override
		public Set<String> getRecentServers() {
			return settingsManager.getRecentlyUsedServers();
		}

		@Override
		public String getRecentServer() {
			return settingsManager.getRecentServer();
		}

		@Override
		public String getRecentUsername() {
			return settingsManager.getRecentUsername();
		}

		@Override
		public String getRecentPassword() {
			return settingsManager.getRecentPassword();
		}

	}

	@Override
	public void editAdhocOptions() {
		AdhocOptionsDialogWindow optionsDialog = new AdhocOptionsDialogWindow(settingsManager.getAdhocOptions());
		optionsDialog.showDialog(gui);
	}

}
