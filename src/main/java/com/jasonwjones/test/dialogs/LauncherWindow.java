package com.jasonwjones.test.dialogs;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.jasonwjones.test.dialogs.MemberSelectionWindow.MemberSelectionWindowDelegate;

public class LauncherWindow extends BasicWindow {

	private LauncherWindowDelegate delegate;
	
	public LauncherWindow() {
		this("Essterm Launcher");
	}
	
	public void setDelegate(LauncherWindowDelegate delegate) {
		this.delegate = delegate;
	}
	
	public LauncherWindow(String title) {
		super(title);
		
		Panel panel = new Panel();
		panel.setPreferredSize(new TerminalSize(30, 7));
		panel.addComponent(new Button("Connect", new Runnable() {
			public void run() {
				connect();
			}
		}));

		panel.addComponent(new Button("Grid Test", new Runnable() {
			public void run() {
				delegate.startAdhocGrid();
			}
		}));

		panel.addComponent(new Button("Member Selector Test", new Runnable() {
			public void run() {
				MemberSelectionWindow msw = new MemberSelectionWindow(new MemberSelectionWindowDelegate() {
					@Override
					public void didChooseDimension(String dimension) {
						//System.out.println("Chose dim: " + dimension);
						
					}});
				getTextGUI().addWindowAndWait(msw);
			}
		}));
		
		panel.addComponent(new Button("Ad hoc Options", new Runnable() {
			public void run() {
				delegate.editAdhocOptions();
//				AdhocOptionsDialogWindow aodw = new AdhocOptionsDialogWindow();
//				getTextGUI().addWindowAndWait(aodw);
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

		setComponent(panel);
	}

	public void connect() {
		//System.out.println("Wants connect");
		delegate.chooseConnection();

	}

	public interface LauncherWindowDelegate {
		
		public void chooseConnection();
		
		public void startAdhocGrid();
		
		public void editAdhocOptions();
		
	}

}
