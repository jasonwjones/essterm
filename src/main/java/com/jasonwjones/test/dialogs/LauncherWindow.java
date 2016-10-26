package com.jasonwjones.test.dialogs;

import org.springframework.stereotype.Component;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.jasonwjones.test.dialogs.MemberSelectionWindow.MemberSelectionWindowDelegate;
import com.jasonwjones.test.models.ChosenConnection;

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
		panel.setPreferredSize(new TerminalSize(30, 5));
		panel.addComponent(new Button("Connect", new Runnable() {
			public void run() {
				connect();
//				ConnectionDialogWindow cdw = new ConnectionDialogWindow("Conn");
//				ChosenConnection conn = cdw.showDialog(getTextGUI());
//				System.out.println("Conn: " + conn);
//				//getTextGUI().addWindowAndWait(cdw);
//				showGridWindow(conn);
			}
		}));

		panel.addComponent(new Button("Grid Test", new Runnable() {
			public void run() {
				GridWindow gridWindow = new GridWindow("Grid");
				getTextGUI().addWindowAndWait(gridWindow);
			}
		}));

		panel.addComponent(new Button("Member Selector Test", new Runnable() {
			public void run() {
				MemberSelectionWindow msw = new MemberSelectionWindow(new MemberSelectionWindowDelegate() {
					@Override
					public void didChooseDimension(String dimension) {
						System.out.println("Chose dim: " + dimension);
						
					}});
				getTextGUI().addWindowAndWait(msw);
			}
		}));
		
		panel.addComponent(new Button("Ad hoc Options", new Runnable() {
			public void run() {
				AdhocOptionsDialogWindow2 aodw = new AdhocOptionsDialogWindow2("Opts");
				getTextGUI().addWindowAndWait(aodw);
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
		System.out.println("Wants connect");
		delegate.chooseConnection();

	}
	
	public void showGridWindow(ChosenConnection connection) {
		System.out.println("Let's show a grid window: " + connection);
		GridWindow gridWindow = new GridWindow("Grid");
		getTextGUI().addWindowAndWait(gridWindow);
	}

	public interface LauncherWindowDelegate {
		
		public void chooseConnection();
		
		public void choseConncection(String connection);
		
	}

}
