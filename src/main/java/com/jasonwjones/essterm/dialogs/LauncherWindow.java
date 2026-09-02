package com.jasonwjones.essterm.dialogs;

import java.util.Arrays;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.LinearLayout.Alignment;
import com.googlecode.lanterna.gui2.Panel;
import com.jasonwjones.essterm.dialogs.adhoc.KeyBindingManager;
import com.jasonwjones.essterm.model.SimpleMemberSelectionWindowModel;

public class LauncherWindow extends BasicWindow {

	private LauncherWindowDelegate delegate;
	
	private boolean devMode = false;
	
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
				.addItem("Member Selection", new Runnable() {
					public void run() {
						MemberSelectionWindow msw = new MemberSelectionWindow(new SimpleMemberSelectionWindowModel(Arrays.asList("Time", "Scenario")));
						getTextGUI().addWindowAndWait(msw);
					}
				})
				.addItem("Key Mappings", new Runnable() {
					public void run() {
						KeyBindingsWindow kbw = new KeyBindingsWindow(KeyBindingManager.defaultKeyBindings());
						getTextGUI().addWindowAndWait(kbw);
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

		if (devMode) {
			panel.addComponent(new Button("Member Selector Test", new Runnable() {
				public void run() {
					MemberSelectionWindow msw = new MemberSelectionWindow(new SimpleMemberSelectionWindowModel(Arrays.asList("Time", "Scenario")));
					getTextGUI().addWindowAndWait(msw);
				}
			}));
		}
		
		
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
