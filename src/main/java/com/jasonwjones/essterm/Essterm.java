package com.jasonwjones.essterm;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.jasonwjones.essterm.dialogs.AdhocGridWindow;
import com.jasonwjones.essterm.dialogs.ConnectionDialogWindow;
import com.jasonwjones.essterm.dialogs.ConnectionDialogWindow.ConnectionDialogModel;
import com.jasonwjones.essterm.dialogs.LauncherWindow;
import com.jasonwjones.essterm.dialogs.LauncherWindow.LauncherWindowDelegate;
import com.jasonwjones.essterm.dialogs.RecentConnectionsWindow;
import com.jasonwjones.essterm.dialogs.adhocoptions.AdhocOptionsDialogWindow;
import com.jasonwjones.essterm.essbase.ConnectionResolver;
import com.jasonwjones.essterm.essbase.RestConnectionResolver;
import com.jasonwjones.essterm.essgrid.RestEssGridFactory;
import com.jasonwjones.essterm.grid.AdhocOptionCapability;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGridException;
import com.jasonwjones.essterm.grid.EssGridFactory;
import com.jasonwjones.essterm.model.ChosenConnection;
import com.jasonwjones.essterm.model.ChosenConnection.Backend;

public class EssTerm implements LauncherWindowDelegate {

	private static final Logger logger = LoggerFactory.getLogger(EssTerm.class);

	@Autowired
	private WindowBasedTextGUI gui;

	@Autowired
	private ConnectionManager connectionManager;

	@Autowired
	private SettingsManager settingsManager;

	// Both null unless the "japi" Maven profile was active at build time (see pom.xml) - a standard
	// release doesn't compile in the classes these beans would be, so there's nothing for Spring to
	// find. required=false lets that be a graceful "JAPI unavailable" rather than a startup failure;
	// the qualifiers pick out the JAPI-backed bean specifically, since both of these interfaces also
	// have a REST-backed implementation in the context.
	@Autowired(required = false)
	@Qualifier("essbaseConnectionResolver")
	private ConnectionResolver japiConnectionResolver;

	@Autowired(required = false)
	@Qualifier("essbaseEssGridFactory")
	private EssGridFactory japiGridFactory;

	@Autowired
	private RestConnectionResolver restConnectionResolver;

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
		connectionDialog.setJapiResolver(japiConnectionResolver);
		connectionDialog.setRestResolver(restConnectionResolver);

		ChosenConnection chosen = connectionDialog.showDialog(gui);
		this.connectionManager.setCurrentConnection(chosen);

		this.settingsManager.addRecentlyUsedServer(chosen.getBackend(), chosen.getServer());
		this.settingsManager.setRecentUsername(chosen.getUsername());
		this.settingsManager.setRecentPassword(chosen.getPassword());
		// Escaping out of the dialog before picking an application/cube still returns a
		// (mostly-empty) ChosenConnection - only a genuinely completed one belongs in "Recents".
		if (chosen.getApplication() != null && chosen.getCube() != null) {
			this.settingsManager.addRecentConnection(chosen);
		}
		saveSettings();
	}

	@Override
	public void showRecentConnections() {
		if (settingsManager.getRecentConnections().isEmpty()) {
			new MessageDialogBuilder()
					.setTitle("Recents")
					.setText("No recent connections yet - use \"Connect to a cube\" first.")
					.build().showDialog(gui);
			return;
		}

		RecentConnectionsWindow window = new RecentConnectionsWindow(settingsManager.getRecentConnections(),
				new RecentConnectionsWindow.RecentConnectionSelectionListener() {
					@Override
					public void onRecentConnectionSelected(ChosenConnection connection) {
						connectionManager.setCurrentConnection(connection);
						settingsManager.addRecentConnection(connection);
						saveSettings();
						startAdhocGrid();
					}
				});
		window.showDialog(gui);
	}

	private void saveSettings() {
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
				ChosenConnection connection = connectionManager.getCurrentConnection();
				EssGridFactory gridFactory;
				if (connection.getBackend() == ChosenConnection.Backend.REST) {
					gridFactory = new RestEssGridFactory();
				} else if (japiGridFactory != null) {
					gridFactory = japiGridFactory;
				} else {
					// Shouldn't happen - the Connect dialog disables the JAPI option entirely when
					// it's unavailable - but a stale ChosenConnection (e.g. from Recents, saved by an
					// earlier japi-profile build) could still name it.
					throw new EssGridException("Essbase Java API support isn't available in this build", null);
				}
				EssGrid grid = gridFactory.createEssGrid(connection);

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
		public Set<String> getRecentServers(Backend backend) {
			return settingsManager.getRecentlyUsedServers(backend);
		}

		@Override
		public String getRecentServer(Backend backend) {
			return settingsManager.getRecentServer(backend);
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
		// No live connection yet at this menu entry point, so there's no backend to ask which
		// options it supports - show everything as available. Whichever backend actually opens a
		// grid later will just silently ignore whatever it doesn't support (see
		// EssGrid#updateCubeViewProperties), the same as if these had been set post-connection.
		AdhocOptionsDialogWindow optionsDialog = new AdhocOptionsDialogWindow(
				settingsManager.getAdhocOptions(), EnumSet.allOf(AdhocOptionCapability.class));
		optionsDialog.showDialog(gui);
		if (optionsDialog.isSaved()) {
			settingsManager.getAdhocOptions().applyFrom(optionsDialog.getValue());
		}
	}

}
