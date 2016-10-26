package com.jasonwjones.test.dialogs;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class DialogTester {

	public static void main(String[] args) throws Exception {
	    Terminal terminal = new DefaultTerminalFactory().createTerminal();
	    Screen screen = new TerminalScreen(terminal);
	    screen.startScreen();
	    
	    // Create gui and start gui
	    MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
	    //gui.addWindowAndWait(new AdhocOptionsDialogWindow2("Ad hoc Options"));
	    
	    gui.addWindowAndWait(StartupDialogWindow.create());
	}

}
