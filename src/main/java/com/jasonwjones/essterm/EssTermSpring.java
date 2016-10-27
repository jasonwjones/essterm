package com.jasonwjones.essterm;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.jasonwjones.essterm.model.EssTermModel;
import com.jasonwjones.test.dialogs.ConnectionDialogWindow;
import com.jasonwjones.test.dialogs.ConnectionDialogWindow.ConnectionDialogModel;
//import com.jasonwjones.test.dialogs.ConnectionDialogWindow.ConnectionWindowDataSource;
import com.jasonwjones.test.dialogs.LauncherWindow;
import com.jasonwjones.test.dialogs.LauncherWindow.LauncherWindowDelegate;
import com.jasonwjones.test.models.ChosenConnection;

public class EssTermSpring implements LauncherWindowDelegate {

	@Autowired
	private Widget widget;
	
	@Autowired
	private WindowBasedTextGUI gui;

	//private LauncherWindow launcherWindow;
	
	@Autowired
	private EssTermModel essTerm;
	
	public static void main(String[] args) {
		// Use the ConfigurableApplicationContext interface so we have access
		// to something that implements Closable (and therefore can use with
		// try-with-resources
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(EssTermConfig.class)) {
			EssTermSpring essTerm = context.getBean(EssTermSpring.class);
			essTerm.run();	
		}
	}

	public void run() {
		System.out.println("Running, widget is: " + widget);
		LauncherWindow launcherWindow = new LauncherWindow();
		launcherWindow.setDelegate(this);
	    //LauncherWindow launcherWindow = new LauncherWindow("Hi");
	    gui.addWindowAndWait(launcherWindow);
	}

	
	@Override
	public void chooseConnection() {
		ConnectionDialogWindow cdw = new ConnectionDialogWindow("Conn");
		cdw.setModel(new ConnectionDialogModelAdapter());
		//cdw.setDataSource(new ConnectionWindowDataSourceAdapter(essTerm));
		ChosenConnection conn = cdw.showDialog(gui);
		
//		System.out.println("Conn: " + conn);
//		//getTextGUI().addWindowAndWait(cdw);
//		showGridWindow(conn);

	}
	
	@Override
	public void choseConncection(String connection) {
		// TODO Auto-generated method stub
		
	}
	
	private class ConnectionDialogModelAdapter implements ConnectionDialogModel {

		@Override
		public String getServer() {
			return "epm11124";
		}

		@Override
		public void setServer(String server) {
			// TODO Auto-generated method stub
			
		}
		
		public List<String> getRecentServers() {
			return Arrays.asList("epm11124", "epm11123");
		}

		@Override
		public List<String> getApplications(String server, String username, String password) {
			return Arrays.asList("Sample", "ASOSamp");
		}

		@Override
		public List<String> getCubes(String application) {
			return Arrays.asList("Basic", "Basic2");
		}
		
	}	
	
}
