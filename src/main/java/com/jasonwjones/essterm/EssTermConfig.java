package com.jasonwjones.essterm;

import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

@Configuration
@ComponentScan(basePackages = {"com.jasonwjones"})
public class EssTermConfig {

	public static final String PROP_FILE = "essterm.properties";
	
	@Bean
	public EssTerm getEssTerm() {
		return new EssTerm();
	}
	
	@Bean
	public ConnectionManager getConnectionManager() {
		return new ConnectionManager();
	}
	
	@Bean
	public WindowBasedTextGUI getTextGUI() throws IOException {
	    DefaultTerminalFactory factory = new DefaultTerminalFactory();
	    // Lanterna already auto-falls-back to a Swing window when it can't detect a real TTY (e.g.
	    // running inside an IDE) - this just lets that same Swing terminal be forced on demand, even
	    // from a genuine terminal, for local testing. See run.sh's --swing flag.
	    if (Boolean.getBoolean("essterm.swing")) {
	        factory.setPreferTerminalEmulator(true);
	    }
	    Terminal terminal = factory.createTerminal();
	    Screen screen = new TerminalScreen(terminal);
	    screen.startScreen();
	    return new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
	}
	
	@Bean
	public SettingsManager settingsManager() {
		return new PropertyFileSettingsManager(new File(PROP_FILE));
	}
	
}
