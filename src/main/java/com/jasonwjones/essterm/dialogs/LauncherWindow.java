package com.jasonwjones.essterm.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;

public class LauncherWindow extends BasicWindow {

	private LauncherWindowDelegate delegate;

	public LauncherWindow() {
		this("Essterm");
	}
	
	public void setDelegate(LauncherWindowDelegate delegate) {
		this.delegate = delegate;
	}
	
	public LauncherWindow(String title) {
		super(title);
		
		Panel panel = new Panel();
		panel.setPreferredSize(new TerminalSize(30, 7));
		
//		Label connectionLabel = new Label("Not connected");
//		Label spacer = new Label("");
//		
//		panel.addComponent(connectionLabel);
//		panel.addComponent(spacer);
		
		ActionListBox actionBox = new ActionListBox()
				.setLayoutData(LinearLayout.createLayoutData(Alignment.Fill))
				.addItem("Connect to a cube", new Runnable() {
					public void run() {
						delegate.chooseConnection();
					}
				})
				.addItem("Start an ad hoc grid", new Runnable() {
					public void run() {
						delegate.startAdhocGrid();
					}
				})
				.addItem("About", new Runnable() {
					public void run() {
						AboutDialog aboutDialog = new AboutDialog("About");
						getTextGUI().addWindowAndWait(aboutDialog);				
					}
				})
				.addItem("Exit", new Runnable() {
					public void run() {
						System.exit(0);
					}
				});
		
		panel.addComponent(actionBox);
		/*
		panel.addComponent(new Button("Connect", new Runnable() {
			public void run() {
				delegate.chooseConnection();
			}
		}));

		panel.addComponent(new Button("Start Ad hoc", new Runnable() {
			public void run() {
				delegate.startAdhocGrid();
			}
		}));

		panel.addComponent(new Button("Ad hoc Options", new Runnable() {
			public void run() {
				delegate.editAdhocOptions();
			}
		}));

		panel.addComponent(new Button("About", new Runnable() {
			@Override
			public void run() {
				AboutDialog aboutDialog = new AboutDialog("About");
				getTextGUI().addWindowAndWait(aboutDialog);				
			}
		}));

		panel.addComponent(new Button("Exit", new Runnable() {
			public void run() {
				System.exit(0);
			}
		}));
		*/

		setComponent(panel);
	}

	public interface LauncherWindowDelegate {
		
		public void chooseConnection();
		
		public void startAdhocGrid();
		
		public void editAdhocOptions();
		
	}

}
