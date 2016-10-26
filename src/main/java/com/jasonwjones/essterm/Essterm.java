package com.jasonwjones.essterm;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.jasonwjones.test.dialogs.LauncherWindow;

public class Essterm {

	private MultiWindowTextGUI gui;
	
	@Autowired
	private LauncherWindow launcherWindow;
	
	public Essterm() throws IOException {
		
	    // Setup terminal and screen layers
	    Terminal terminal = new DefaultTerminalFactory().createTerminal();
	    Screen screen = new TerminalScreen(terminal);
	    screen.startScreen();

	    gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
	    
	    /*
	    // Create window to hold the panel
	    BasicWindow window = new BasicWindow();
		
		ActionListDialog dialog = new ActionListDialogBuilder()
		.setTitle("Essterm")
		.setDescription("This is desc and another line")
		.addAction("Connect", new Runnable() {

			public void run() {
				System.out.println("Want conncet");
			}})
		.addAction("Configure ad hoc options", new Runnable() {
			public void run() {
				new AdhocOptionsDialogWindow2("Opts").showDialog(gui);
			}})
		.addAction("About", null)
		.addAction("Exit", new Runnable() {
			public void run() {
				System.exit(0);
			}})
		
		.build();
		
		//window.
		 * 
		 */
	    
	    //LauncherWindow launcherWindow = new LauncherWindow("Hi");
	    
	    gui.addWindowAndWait(launcherWindow);
		
	}
	
	public static void main(String[] args) throws IOException {
		new Essterm();
	}

}
