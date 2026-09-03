package com.jasonwjones.essterm.dialogs;

import java.util.Arrays;
import java.util.List;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.jasonwjones.essterm.model.ChosenConnection;

/**
 * Lets someone jump straight to a previously-used connection instead of stepping back through the
 * Connect dialog. Selecting one closes this window and hands the connection to the given listener,
 * which is expected to open the ad hoc grid for it right away.
 */
public class RecentConnectionsWindow extends BasicWindow {

	public interface RecentConnectionSelectionListener {

		void onRecentConnectionSelected(ChosenConnection connection);

	}

	public RecentConnectionsWindow(List<ChosenConnection> recentConnections, RecentConnectionSelectionListener listener) {
		super("Recent Connections");
		setCloseWindowWithEscape(true);
		setHints(Arrays.asList(Hint.CENTERED));

		int rows = Math.min(Math.max(recentConnections.size(), 3), 10);
		ActionListBox listBox = new ActionListBox(new TerminalSize(50, rows));
		for (ChosenConnection connection : recentConnections) {
			listBox.addItem(label(connection), new Runnable() {
				@Override
				public void run() {
					close();
					listener.onRecentConnectionSelected(connection);
				}
			});
		}

		setComponent(listBox);
	}

	public void showDialog(WindowBasedTextGUI textGUI) {
		textGUI.addWindowAndWait(this);
	}

	private static String label(ChosenConnection connection) {
		return String.format("%s.%s - %s@%s (%s)", connection.getApplication(), connection.getCube(),
				connection.getUsername(), connection.getServer(), connection.getBackend());
	}

}
